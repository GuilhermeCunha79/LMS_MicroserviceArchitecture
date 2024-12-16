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
package pt.psoft.g1.psoftg1.shared.api;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * code based on
 * https://github.com/callicoder/spring-boot-file-upload-download-rest-api-example
 *
 *
 */
@Schema(description = "Metadata about the uploaded file")
public class UploadFileResponse {

	private final String fileName;

	@Schema(description = "Absolute URL of the file")
	private final String fileDownloadUri;

	@Schema(description = "Media type")
	private final String fileType;

	@Schema(description = "File size in bytes")
	private final long size;

	public UploadFileResponse(final String fileName, final String fileDownloadUri, final String fileType,
                              final long size) {
		this.fileName = fileName;
		this.fileDownloadUri = fileDownloadUri;
		this.fileType = fileType;
		this.size = size;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileDownloadUri() {
		return fileDownloadUri;
	}

	public String getFileType() {
		return fileType;
	}

	public long getSize() {
		return size;
	}
}
