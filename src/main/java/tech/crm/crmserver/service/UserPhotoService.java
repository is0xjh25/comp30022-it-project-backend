package tech.crm.crmserver.service;

import tech.crm.crmserver.dao.UserPhoto;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.crm.crmserver.dto.UserPhotoDTO;

import java.util.List;

/**
 * <p>
 *  service
 * </p>
 *
 * @author Lingxiao
 * @since 2021-10-10
 */
public interface UserPhotoService extends IService<UserPhoto> {

    /**
     * insert or update a user photo<br/>
     * @param userPhoto the photo of user
     */
    public void insertOrUpdate(UserPhoto userPhoto);

    /**
     * get user photos by user id list<br/>
     * will encoded with base64
     * @param userIdList the id list of user
     * @return List<UserPhotoDTO> list of photo with photo encoded
     */
    public List<UserPhotoDTO> getByUserIdList(List<Integer> userIdList);


    /**
     * get user photo by user id<br/>
     * will encoded with base64
     * @param userId the id list of user
     * @return UserPhotoDTO photo with photo encoded
     */
    public UserPhotoDTO getByUserId(Integer userId);
}
