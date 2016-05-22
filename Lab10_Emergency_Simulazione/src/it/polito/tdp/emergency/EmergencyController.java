/**
 * Sample Skeleton for 'Emergency.fxml' Controller Class
 */

package it.polito.tdp.emergency;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.emergency.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EmergencyController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtNome"
    private TextField txtNome; // Value injected by FXMLLoader

    @FXML // fx:id="txtOre"
    private TextField txtOre; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void addDottore(ActionEvent event) {
    	if(txtOre.getText()!= null && txtNome.getText()!=null) {
    		if(model.addMedico(txtNome.getText(), Integer.parseInt((txtOre.getText()))))
    			txtResult.setText("Il medico "+txtNome.getText()+" è stato aggiunto\n");
    		else
    			txtResult.setText("Non è stato possibile aggiungere tale medico\n");
    	} else {
    		txtResult.setText("Non hai inserito tutti i campi!\n");
    	}

    }

    @FXML
    void goSimu(ActionEvent event) {
    	txtResult.setText(model.stub());

    }
    
	public void setModel(Model model) {
		this.model = model;	
	}

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtNome != null : "fx:id=\"txtNome\" was not injected: check your FXML file 'Emergency.fxml'.";
        assert txtOre != null : "fx:id=\"txtOre\" was not injected: check your FXML file 'Emergency.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Emergency.fxml'.";

    }

}
