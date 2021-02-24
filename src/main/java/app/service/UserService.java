package app.service;

import app.dto.FindUserDTO;
import app.entities.User;
import java.util.List;
import org.springframework.data.domain.Page;

public interface UserService {

    List<User> getAll();

    void save(User user);

    User getById(long id);

    void deleteById(long id);

    Page<User> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    Page<User> findUserPaginated(FindUserDTO findUserDTO, int pageNo, int pageSize, String sortField, String sortDirection);
}
