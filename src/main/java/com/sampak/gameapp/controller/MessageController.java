

import java.util.List;

import com.sampak.gameapp.dto.SocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageController {


    @CrossOrigin
    @GetMapping("/{room}")
    public ResponseEntity<String> getMessages(@PathVariable String room) {
        return ResponseEntity.ok("Ok");
    }

}