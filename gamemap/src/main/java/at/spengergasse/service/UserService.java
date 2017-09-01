package at.spengergasse.service;

import at.spengergasse.domain.User;
import at.spengergasse.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import javax.validation.Valid;


// TODO Aspect for Exception Handling

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;


    /**
     * fetches a user record and lazily loaded maps
     * @throws ServiceException
     */
    public User findUser( Long userId ) {

        try {
            return userRepository
                .findById( userId )
                .orElseThrow( ServiceException.ofUserNotFound );

        } catch ( Exception ex ) {
            throw new ServiceException( ex );
        }
    }


    /**
     * fetches a user record and lazily loaded maps
     * @throws ServiceException
     */
    public User findUser_withEagerFetchingMaps( Long userId ) {

        try {
            return userRepository
                .findById_eagerFetch( userId )
                .orElseThrow( ServiceException.ofUserNotFound );

        } catch ( Exception ex ) {
            throw new ServiceException( ex );
        }
    }


    /**
     * @throws ServiceException
     */
    @Transactional(readOnly = false)
    public User saveUser( @Valid User user ) {

        try {
            return userRepository.save( user );

        } catch ( PersistenceException ex ) {
            throw new ServiceException( ex );
        }
    }

    /**
     * @throws ServiceException
     */
    @Transactional(readOnly = false)
    public void deleteUsers() {

        try {
            userRepository.deleteAll();

        } catch ( Exception ex ) {
            throw new ServiceException( ex );
        }
    }
}
