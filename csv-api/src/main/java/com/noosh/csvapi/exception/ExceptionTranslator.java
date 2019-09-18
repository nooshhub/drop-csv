package com.noosh.csvapi.exception;

import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
// TODO: try to catch all file upload related exception and handle it
@RestControllerAdvice
public class ExceptionTranslator {

    //StandardServletMultipartResolver
    @ExceptionHandler(FileUploadBase.SizeLimitExceededException.class)
    public String handleError2(FileUploadBase.SizeLimitExceededException e) {
//, RedirectAttributes redirectAttributes
//        redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
        return "message: File too large 3!";

    }

    //StandardServletMultipartResolver
    @ExceptionHandler(MultipartException.class)
    public String handleError1(MultipartException e) {
//, RedirectAttributes redirectAttributes
//        redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
        return "message: File too large 1!";

    }

    //CommonsMultipartResolver
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.OK)
    public String handleMaxSizeException(
            MaxUploadSizeExceededException exc,
            HttpServletRequest request,
            HttpServletResponse response) {

        return "message: File too large 2!";
    }
}
