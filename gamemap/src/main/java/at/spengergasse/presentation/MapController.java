package at.spengergasse.presentation;

import at.spengergasse.domain.GameMap;
import at.spengergasse.domain.GameMode;
import at.spengergasse.domain.Size;
import at.spengergasse.service.MapService;
import at.spengergasse.service.ServiceException;
import at.spengergasse.service.command.AddMapCommand;
import at.spengergasse.service.command.UpdateMapCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.sql.rowset.serial.SerialException;
import javax.validation.Valid;


/***************************
 * URL Handling            *
 ***************************
 *
 * GET /user/1/map/2/update
 * POST /user/1/map/2/update
 *
 * GET /user/1/map/new
 * POST /user/1/map/new
 *
 * POST /user/1/map/2/remove
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/{userId}/map")
public class MapController {

    private final MapService mapService;


    @GetMapping("/{mapId}/update")
    public String showMapFormForUpdate( @PathVariable Long mapId, Model model ) {

        GameMap map = mapService.findMap( mapId );

        model.addAttribute( "map", map );
        model.addAttribute( "userId", map.getCreater().getId() );

        return "map-form";
    }


    @PostMapping("/{mapId}/update")
    public String handleMapFormSubmitForUpdate(Model model,
        @ModelAttribute("map") @Valid UpdateMapCommand command, BindingResult result ) {

        if ( result.hasErrors() ) {
            model.addAttribute( "userId", command.getUserId() );
           return "map-form";
        }

        mapService.updateMap( command );

        return "redirect:/user/" +command.getUserId()+ "/show";
    }


    @GetMapping("/new")
    String showMapFormForAdd( @PathVariable Long userId, Model model ) {

        model.addAttribute( "map", null );
        model.addAttribute( "userId", userId );

        return "map-form-new";
    }


    @PostMapping("/new")
    public String handleMapFormSubmitForAdd( Model model, @PathVariable Long userId,
        @ModelAttribute("map") @Valid AddMapCommand command, BindingResult result ) {

        if ( result.hasErrors() ) {
            model.addAttribute( "userId", userId );
            return "map-form-new";
        }

        mapService.addMap( command );

        return "redirect:/user/" +command.getUserId()+ "/show";
    }


    @PostMapping("/{mapId}/remove")
    public String removeMap( @PathVariable Long userId, @PathVariable Long mapId ) {

        mapService.removeMap( mapId );

        return "redirect:/user/" +userId+ "/show";
    }


    @ExceptionHandler(ServiceException.class)
    public String handleServiceException() {
        return "error";
    }
}
