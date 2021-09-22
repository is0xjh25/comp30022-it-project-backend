package tech.crm.crmserver.common.constants;

/**
 * <p>
 *  Exception Message Constants
 * </p>
 * @author Lingxiao Li
 * @since 2021-09-18
 */
public class ExceptionMessageConstants {

    //exception messages
    public static final String DEPARTMENT_ALREADY_EXIST_EXCEPTION = "Department already exist.";
    public static final String LOGIN_BAD_CREDENTIALS_EXCEPTION  = "The user email or password is not correct.";
    public static final String NOT_ENOUGH_PERMISSION_EXCEPTION  = "Sorry you do not have enough permissions to access it!";
    public static final String ORGANIZATION_NOT_EXIST_EXCEPTION  = "Organization do not exist!";
    public static final String USER_ALREADY_EXIST_EXCEPTION  = "Same email already exist!";
    public static final String USER_ALREADY_IN_DEPARTMENT_EXCEPTION  = "This user already in the department";
    public static final String USER_NOT_IN_DEPARTMENT_EXCEPTION  = "You are not a member of this department";


    private ExceptionMessageConstants() {
    }
}
