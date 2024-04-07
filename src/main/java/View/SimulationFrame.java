package View;

import Model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class SimulationFrame {

    public void addClientToVBox(VBox vbox, Task task){
        Label label = new Label(task.toString());
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        vbox.getChildren().add(label);
    }

    public void clearWaitingClientList(VBox vbox){
        vbox.getChildren().clear();
    }


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

    public VBox getvBoxClients() {
        return vBoxClients;
    }

    public VBox getvBoxQueue1() {
        return vBoxQueue1;
    }

    public VBox getvBoxQueue2() {
        return vBoxQueue2;
    }
    public VBox getvBoxQueue3() {
        return vBoxQueue3;
    }

    public VBox getvBoxQueue4() {
        return vBoxQueue4;
    }
    public VBox getvBoxQueue5() {
        return vBoxQueue5;
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
    @FXML
    private VBox vBoxClients;
}
