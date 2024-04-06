package View;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SimulationFrame {

    public void setLblValidateData(String str){
        lblValidateData.setText(str);
    }

    public Integer getNumberOfClients(){
        if (txtNrClients.getText().isEmpty()){
            return 0;
        }
        try{
            return Integer.parseInt(txtNrClients.getText());
        }catch (NumberFormatException e){
            return 0;
        }
    }

    public Integer getActiveQueues(){
        if (txtNrQueues.getText().isEmpty()){
            return 0;
        }
        try{
            return Integer.parseInt(txtNrQueues.getText());
        }catch (NumberFormatException e){
            return 0;
        }
    }

    public Integer getSimulationInterval(){
        if (txtSimulationInterval.getText().isEmpty())
            return 0;

        try{
            return Integer.parseInt(txtSimulationInterval.getText());
        }catch (NumberFormatException e) {
            return 0;
        }
   }

    public Integer getMinimumArrivalTime(){
        if (txtMinArrival.getText().isEmpty()){
            return 0;
        }
        try{
            return Integer.parseInt(txtMinArrival.getText());
        }catch (NumberFormatException e){
            return 0;
        }
    }

    public Integer getMaximumArrivalTime(){
        if (txtMaxArrival.getText().isEmpty()){
            return 0;
        }
        try{
            return Integer.parseInt(txtMaxArrival.getText());
        }catch (NumberFormatException e){
            return 0;
        }
    }

    public Integer getMinimumServiceTime(){
        if (txtMinService.getText().isEmpty()){
            return 0;
        }
        try{
            return Integer.parseInt(txtMinService.getText());
        }catch (NumberFormatException e){
            return 0;
        }
    }

    public Integer getMaximumServiceTime(){
        if (txtMaxService.getText().isEmpty()){
            return 0;
        }
        try{
            return Integer.parseInt(txtMaxService.getText());
        }catch (NumberFormatException e){
            return 0;
        }
    }

    public Button getBtnStartSimulation() {
        return btnStartSimulation;
    }

    public Button getBtnValidateData() {
        return btnValidateData;
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
    private Label lblValidateData;
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
