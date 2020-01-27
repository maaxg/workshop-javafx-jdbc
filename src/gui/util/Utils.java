package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
                //A method that returns the currentStage
    public static Stage currentStage(ActionEvent ev){
        return (Stage) ((Node) ev.getSource()).getScene().getWindow();
    }

    public static Integer tryParseToInt(String str){
        try {
            return Integer.parseInt(str);
        }catch (NumberFormatException ex){
            return null;
        }

    }

}
