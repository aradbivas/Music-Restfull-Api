package restapi.webapp;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

public interface UserRepo extends CrudRepository<User,Long> {
    User findByUserName(String UserName);
    List<User> findByEmail(String Email);
    List<User> findByCreationDate(Date date);
}
