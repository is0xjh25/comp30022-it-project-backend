package tech.crm.crmserver.common.constants;

/**
 * <p>
 *  Exception Message Constants
 * </p>
 * @author Lingxiao Li
 * @since 2021-09-18
 */
public final class ExceptionMessageConstants {

    //exception messages
    public static final String DEPARTMENT_ALREADY_EXIST_EXCEPTION = "Department already exist.";
    public static final String DEPARTMENT_NOT_EXIST_EXCEPTION = "Department do not exist.";

    public static final String LOGIN_BAD_CREDENTIALS_EXCEPTION  = "The user email or password is not correct.";

    public static final String NOT_ENOUGH_PERMISSION_EXCEPTION  = "Sorry you do not have enough permissions to access it!";

    public static final String ORGANIZATION_NOT_EXIST_EXCEPTION  = "Organization do not exist!";
    public static final String ORGANIZATION_NOT_FOUND_EXCEPTION  = "No match organization is found!";
    public static final String ORGANIZATION_ALREADY_EXIST_EXCEPTION  = "Organization with same name exists!";
    public static final String FAIL_TO_CREATE_ORGANIZATION_EXCEPTION  = "Fail to create organization!";

    public static final String USER_ALREADY_EXIST_EXCEPTION  = "Same email already exist!";
    public static final String USER_NOT_EXIST_EXCEPTION  = "User do not exists!";
    public static final String USER_NOT_ACTIVE_EXCEPTION  = "This account is not active! Please check your email!";
    public static final String USER_ALREADY_IN_DEPARTMENT_EXCEPTION  = "This user already in the department!";
    public static final String USER_ALREADY_IN_ORGANIZATION_EXCEPTION  = "This user already in the organization!";
    public static final String USER_NOT_IN_DEPARTMENT_EXCEPTION  = "You are not a member of this department!";

    public static final String CONTACT_NOT_EXIST_EXCEPTION  = "Contact do not exist!";
    public static final String CONTACT_NOT_FOUND_EXCEPTION  = "No match contact is found!";
    public static final String CONTACT_ALREADY_EXIST_EXCEPTION  = "Contact already exist!";
    public static final String CONTACT_FAIL_ADDED_EXCEPTION  = "Fail to adding a contact!";
    public static final String CONTACT_FAIL_UPDATE_EXCEPTION  = "Fail to update a contact!";
    public static final String CONTACT_FAIL_DELETE_EXCEPTION  = "Fail to delete a contact!";

    public static final String ID_NOT_EXIST_EXCEPTION  = "Id can not be null!";

    public static final String TODOLIST_FAIL_ADDED_EXCEPTION = "Fail to add a todoList data";

    public static final String EVENT_FAIL_ADDED_EXXCEPTION = "Fail to add a event data";
    public static final String EVENT_FAIL_QUERY_EXXCEPTION = "Fail to get a event data by id";



    private ExceptionMessageConstants() {
    }
}
