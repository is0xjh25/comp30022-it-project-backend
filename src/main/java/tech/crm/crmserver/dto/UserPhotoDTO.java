package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *  DTO for get User photo<br/>
 *  photo was encoded with base64
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPhotoDTO {

    private Integer userId;

    private String photoName;

    private String photo;

}
