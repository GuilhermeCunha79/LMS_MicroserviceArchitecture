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
package pt.psoft.g1.psoftg1.exceptions;

import java.net.MalformedURLException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author pgsousa
 *
 */
@ResponseStatus(code = HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public ConflictException(final String string) {
		super(string);
	}

	public ConflictException(final String string, final MalformedURLException ex) {
		super(string, ex);
	}

	public ConflictException(final Class<?> clazz, final long id) {
		super(String.format("Entity %s with id %d not found", clazz.getSimpleName(), id));
	}

	public ConflictException(final Class<?> clazz, final String id) {
		super(String.format("Entity %s with id %s not found", clazz.getSimpleName(), id));
	}
}
