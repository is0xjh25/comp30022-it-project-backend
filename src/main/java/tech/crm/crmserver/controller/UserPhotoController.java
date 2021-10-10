package tech.crm.crmserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.dao.UserPhoto;
import tech.crm.crmserver.dto.UserPhotoDTO;
import tech.crm.crmserver.service.UserPhotoService;
import tech.crm.crmserver.service.UserService;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * <p>
 *  controller
 * </p>
 *
 * @author Lingxiao
 * @since 2021-10-10
 */
@RestController
@RequestMapping("/userPhoto")
public class UserPhotoController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserPhotoService userPhotoService;

    @PostMapping("/uploadPhoto")
    public ResponseResult<Object> uploadPhoto(@RequestParam("photo") MultipartFile photo) throws IOException {
        UserPhoto userPhoto = new UserPhoto();
        userPhoto.setUserId(userService.getId());
        userPhoto.setPhotoName(photo.getOriginalFilename());
        userPhoto.setPhoto(photo.getBytes());
        userPhotoService.insertOrUpdate(userPhoto);
        return ResponseResult.suc("Successfully save the photo");
    }

    @GetMapping("/getPhoto")
    public ResponseResult<Object> getPhoto() {
        UserPhotoDTO userPhotoDTO = userPhotoService.getByUserId(userService.getId());
        return ResponseResult.suc("Successfully get the photo",userPhotoDTO);
    }

    @GetMapping(value = "/image",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] test() throws Exception {
        UserPhoto userPhoto = userPhotoService.getById(userService.getId());
        byte[] bytes = userPhoto.getPhoto();
        return bytes;
    }
}

