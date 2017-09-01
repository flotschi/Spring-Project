package at.spengergasse.service;

import at.spengergasse.domain.GameMap;
import at.spengergasse.domain.User;
import at.spengergasse.persistence.GameMapRepository;
import at.spengergasse.persistence.UserRepository;
import at.spengergasse.service.command.AddMapCommand;
import at.spengergasse.service.command.UpdateMapCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

import static at.spengergasse.service.command.CommandMapper.createMapOf;


// TODO Aspect for Exception Handling

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MapService {

    private final UserRepository userRepository;
    private final GameMapRepository mapRepository;


    /**
     * @throws ServiceException
     */
    public GameMap findMap( Long mapId ) {

        try {
            return mapRepository
                .findById( mapId )
                .orElseThrow( ServiceException.ofMapNotFound );

        } catch ( Exception ex ) {
            throw new ServiceException( ex );
        }
    }


    /**
     * @throws ServiceException
     */
    @Transactional(readOnly = false)
    public GameMap addMap( @Valid AddMapCommand command ) {

        try {
            User user = userRepository
                .findById( command.getUserId() )
                .orElseThrow( ServiceException.ofUserNotFound );

            GameMap map = createMapOf( command );

            // user is a managed entity in transaction
            // persistence provider propagates any change in the user/map to the database
            user.addMap( map );

            return map;

        } catch ( Exception ex ) {
            throw new ServiceException( ex );
        }
    }


    /**
     * @throws ServiceException
     */
    @Transactional(readOnly = false)
    public GameMap updateMap( @Valid UpdateMapCommand command ) {

        try {
            GameMap map = mapRepository
                .findById( command.getId() )
                .orElseThrow( ServiceException.ofMapNotFound );

            map.getCreater().updateMap( createMapOf( command ) );

            return map;

        } catch ( Exception ex ) {
            throw new ServiceException( ex );
        }
    }


    /**
     * @throws ServiceException
     */
    @Transactional(readOnly = false)
    public GameMap removeMap( Long mapId ) {

        try {
            GameMap map = mapRepository
                .findById( mapId )
                .orElseThrow( ServiceException.ofMapNotFound );

            map.getCreater().removeMap( map );

            return map;

        } catch ( Exception ex ) {
            throw new ServiceException( ex );
        }
    }
}
