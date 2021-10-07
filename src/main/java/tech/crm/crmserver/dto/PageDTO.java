package tech.crm.crmserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * <p>
 *  DTO for page
 * </p>
 *
 * @author Lingxiao
 * @since 2021-10-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO {

    @NotNull(message = "size cannot be null")
    @Positive(message = "size should be positive")
    private Integer size;

    @NotNull(message = "current page cannot be null")
    @Positive(message = "current page should be positive")
    private Integer current;
}
