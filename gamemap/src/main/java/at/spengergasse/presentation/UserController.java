package at.spengergasse.presentation;

import at.spengergasse.domain.User;
import at.spengergasse.service.ServiceException;
import at.spengergasse.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


/***************************
 * URL Handling            *
 ***************************
 *
 * GET /user/1/show
 */

@Controller
@RequiredArgsConstructor
@RequestMapping( "/user" )
public class UserController {

    private final UserService userService;


    @GetMapping("/{userId}/show")
    public String showUserDetails( @PathVariable Long userId, Model model ) {

        // User user = userService.findUser( userId );
        User user = userService.findUser_withEagerFetchingMaps( userId );

        model.addAttribute( "user", user );

        return "user-overview";
    }


    @ExceptionHandler(ServiceException.class)
    public String handleServiceException() {
        return "error";
    }
}
