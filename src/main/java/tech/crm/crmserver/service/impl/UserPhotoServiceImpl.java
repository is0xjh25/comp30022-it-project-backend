package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import tech.crm.crmserver.dao.UserPhoto;
import tech.crm.crmserver.dto.UserPhotoDTO;
import tech.crm.crmserver.exception.BadPhotoException;
import tech.crm.crmserver.mapper.UserPhotoMapper;
import tech.crm.crmserver.service.UserPhotoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * <p>
 *  serviceImpl
 * </p>
 *
 * @author Lingxiao
 * @since 2021-10-10
 */
@Service
public class UserPhotoServiceImpl extends ServiceImpl<UserPhotoMapper, UserPhoto> implements UserPhotoService {

    /**
     * insert or update a user photo<br/>
     *
     * @param userPhoto the photo of user
     */
    @Override
    public void insertOrUpdate(UserPhoto userPhoto) {
        //check the input
        if(userPhoto == null || userPhoto.getUserId() == null
                || userPhoto.getPhotoName() == null || userPhoto.getPhoto() == null){
            throw new BadPhotoException();
        }

        UserPhoto photo = getById(userPhoto.getUserId());
        //insert
        if(photo == null){
            save(userPhoto);
        }
        //update
        else {
            updateById(userPhoto);
        }
    }

    /**
     * get user photos by user id list<br/>
     * will encoded with base64
     *
     * @param userIdList the id list of user
     * @return List<UserPhotoDTO> list of photo with photo encoded
     */
    @Override
    public List<UserPhotoDTO> getByUserIdList(List<Integer> userIdList) {
        QueryWrapper<UserPhoto> wrapper = new QueryWrapper<>();

        wrapper.in("user_id",userIdList);
        List<UserPhoto> userPhotos = baseMapper.selectList(wrapper);
        return UserPhotoListToDTOList(userPhotos);

    }

    /**
     * get user photo by user id<br/>
     * will encoded with base64
     *
     * @param userId the id list of user
     * @return UserPhotoDTO photo with photo encoded
     */
    @Override
    public UserPhotoDTO getByUserId(Integer userId) {
        if(userId == null){
            return null;
        }
        return UserPhotoToDTO(getById(userId));
    }

    /**
     * convert a user photo to user photo dto<br/>
     * will encoded with base64
     * @param photo user photo
     * @return user photo dto
     */
    private UserPhotoDTO UserPhotoToDTO(UserPhoto photo){
        //check
        if(photo == null || photo.getUserId() == null
                || photo.getPhotoName() == null || photo.getPhoto() == null){
            throw new BadPhotoException();
        }
        //convert
        UserPhotoDTO userPhotoDTO = new UserPhotoDTO();
        userPhotoDTO.setUserId(photo.getUserId());
        userPhotoDTO.setPhotoName(photo.getPhotoName());
        userPhotoDTO.setPhoto(Base64.getEncoder().encodeToString(photo.getPhoto()));
        return userPhotoDTO;
    }

    /**
     * convert a list of user photos to user photo DTOs<br/>
     * will encoded with base64
     * @param photoList list of user photos
     * @return user photo dto
     */
    private List<UserPhotoDTO> UserPhotoListToDTOList(List<UserPhoto> photoList){
        ArrayList<UserPhotoDTO> userPhotoDTOList = new ArrayList<>();
        for (UserPhoto photo:photoList){
            UserPhotoDTO userPhotoDTO = UserPhotoToDTO(photo);
            if(userPhotoDTO != null){
                userPhotoDTOList.add(userPhotoDTO);
            }
        }
        return userPhotoDTOList;
    }
}
