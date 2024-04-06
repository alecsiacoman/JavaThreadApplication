package View;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SimulationFrame {

    public Integer getNumberOfClients(){
        return Integer.parseInt(txtNrClients.getText());
    }

    public Integer getActiveQueues(){
        return Integer.parseInt(txtNrQueues.getText());
    }

    public Integer getSimulationInterval(){
        return Integer.parseInt(txtSimulationInterval.getText());
    }

    public Integer getMinimumArrivalTime(){
        return Integer.parseInt(txtMinArrival.getText());
    }

    public Integer getMaximumArrivalTime(){
        return Integer.parseInt(txtMaxArrival.getText());
    }

    public Integer getMinimumServiceTime(){
        return Integer.parseInt(txtMinService.getText());
    }

    public Integer getMaximumServiceTime(){
        return Integer.parseInt(txtMaxService.getText());
    }

    @FXML
    private TextField txtNrClients;
    @FXML
    private TextField txtNrQueues;
    @FXML
    private TextField txtSimulationInterval;
    @FXML
    private TextField txtMinArrival;
    @FXML
    private TextField txtMaxArrival;
    @FXML
    private TextField txtMinService;
    @FXML
    private TextField txtMaxService;
    @FXML
    private Button btnValidateData;
    @FXML
    private Button btnStartSimulation;
    @FXML
    private VBox vBoxQueue1;
    @FXML
    private VBox vBoxQueue2;
    @FXML
    private VBox vBoxQueue3;
    @FXML
    private VBox vBoxQueue4;
    @FXML
    private VBox vBoxQueue5;

}
