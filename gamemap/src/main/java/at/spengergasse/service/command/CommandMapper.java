package at.spengergasse.service.command;


import at.spengergasse.domain.GameMap;


public class CommandMapper {

    public static GameMap createMapOf( AddMapCommand command ) {
        return GameMap.builder()
            .name( command.getName() )
            .size( command.getSize() )
            .gameMode( command.getGameMode() )
            .price( command.getPrice() )
            .build();
    }


    public static GameMap createMapOf( UpdateMapCommand command ) {
        return GameMap.builder()
            .id( command.getId() )
            .version( command.getVersion() )
            .name( command.getName() )
            .size( command.getSize() )
            .gameMode( command.getGameMode() )
            .price( command.getPrice() )
            .build();
    }
}
