/*
 * Copyright (c) 2022-2024 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package pt.psoft.g1.psoftg1.shared.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.exceptions.FileStorageException;
import pt.psoft.g1.psoftg1.shared.api.UploadFileResponse;
import pt.psoft.g1.psoftg1.shared.model.FileUtils;

/**
 * <p>
 * code based on
 * https://github.com/callicoder/spring-boot-file-upload-download-rest-api-example
 *
 *
 */
@RequiredArgsConstructor
@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private long photoMaxSize;
    private final String[] validImageFormats = {"image/png", "image/jpeg"};

    @Autowired
    public FileStorageService(final FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        this.photoMaxSize = fileStorageProperties.getPhotoMaxSize();

        try {
            Files.createDirectories(fileStorageLocation);
        } catch (final Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
                    ex);
        }
    }

    public String storeFile(final String prefix, final MultipartFile file) {
        //final String fileName = prefix + "_" + determineFileName(file);
        //files will contain only the generated uuid passed as prefix
        final String fileName = prefix + "." + getExtension(file.getOriginalFilename()).orElse("");

        // Copy file to the target location (Replacing existing file with the same name)
        try {
            final Path targetLocation = fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (final IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public void deleteFile(String file) {
        if(file == null) {
            throw new IllegalArgumentException("Received null reference to file path");
        }

        Path filePath = Paths.get(this.fileStorageLocation + "/" + file);
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new NotFoundException("Reader photo could not be deleted as his photo file doesn't exist.");
        }
    }

    public byte[] getFile(final String fileName) {
        String photoPathString = this.fileStorageLocation + "/" + fileName;
        Path photoPath = Paths.get(photoPathString);
        byte[] image = null;
        try {
            image = Files.readAllBytes(photoPath);
        } catch(IOException e) {
            return null;
        }

        return image;
    }

    //Returns the string of the fileName of the file (UUID.FILE_FORMAT) stored in the uploads folder | null for error or no photo
    public String getRequestPhoto(MultipartFile file) {
        UploadFileResponse up = null;
        if(file != null) {
            if(file.getSize() > photoMaxSize) {
                throw new ValidationException("Attached photo can't be bigger than " + photoMaxSize + " bytes");
            }

            int formatIndex = -1;
            String fileContentHeader = file.getContentType();

            if(fileContentHeader == null) {
                throw new ValidationException("Unknown file content header");
            }

            for(int i = 0; i < validImageFormats.length; i++) {
                if(!fileContentHeader.equals(validImageFormats[i])) {
                    continue;
                }

                formatIndex = i;
                break;
            }

            if(formatIndex == -1) {
                throw new ValidationException("Images can only be png or jpeg");
            }

            String photoUUID = UUID.randomUUID().toString();

            try {
                up = FileUtils.doUploadFile(this, photoUUID, file);
            } catch (Exception e) {
                return null;
                //throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            //String fileFormat = validImageFormats[formatIndex].split("/")[1];
            String originalFileName = file.getOriginalFilename();
            String fileFormat = originalFileName.substring(originalFileName.lastIndexOf('.')+1);
            return photoUUID+"."+fileFormat;
        }

        return null;
    }

    private String determineFileName(final MultipartFile file) {
//		// Normalize file name
//		final String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//		// Check if the file's name contains invalid characters
//		if (fileName.contains("..")) {
//			throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
//		}
//		return fileName;

        return UUID.randomUUID().toString() + "." + getExtension(file.getOriginalFilename()).orElse("");
    }

    public Optional<String> getExtension(final String filename) {
        return Optional.ofNullable(filename).filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    public Resource loadFileAsResource(final String fileName) {
        try {
            final Path filePath = fileStorageLocation.resolve(fileName).normalize();
            final Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }
            throw new NotFoundException("File not found " + fileName);
        } catch (final MalformedURLException ex) {
            throw new NotFoundException("File not found " + fileName, ex);
        }
    }
}
