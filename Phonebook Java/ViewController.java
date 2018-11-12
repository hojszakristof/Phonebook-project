package phonebook;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class ViewController implements Initializable {
    
    @FXML
    TableView table;
    @FXML
    TextField inputLastName;
    @FXML
    TextField inputFirstName;
    @FXML
    TextField inputEmail;
    @FXML
    Button addNewContactButton;
    @FXML
    StackPane menuPane;
    @FXML
    Pane contactPane;
    @FXML
    Pane exportPane;
    @FXML
    TextField inputExportName;
    @FXML
    Button exportButton;
    
    private final String MENU_CONTACT = "Kontaktok";
    private final String MENU_LIST = "Lista";
    private final String MENU_EXPORT = "Export";
    private final String MENU_EXIT = "Kilépés";
    
    private final ObservableList<Person> data =
            FXCollections.observableArrayList(
            new Person("Szabó", "Gyula", "gyuszi@example.com"),
            new Person("Bourne", "Jason", "wat@example.com"));
            
    
    @FXML
    private void addContact(ActionEvent event){
        String email = inputEmail.getText();
        if(email.length() > 5 && email.contains("@") && email.contains(".")){
            data.add(new Person(inputLastName.getText(), inputFirstName.getText(), inputEmail.getText()));
            inputLastName.clear();
            inputFirstName.clear();
            inputEmail.clear();
        }
    }
    
    @FXML
    private void exportList(ActionEvent event){
        String fileName = inputExportName.getText();
        fileName = fileName.replaceAll("\\s+", "");
        if(fileName != null && !fileName.equals("")){
        PDFGeneration pdfCreator = new PDFGeneration();
        pdfCreator.pdfGeneration(fileName, data);
            }
        }
    
        
    
    public void setTableData(){
        TableColumn lastNameCol = new TableColumn("Vezetéknév");            //létrehozzuk egy táblaoszlopot (vezetéknév oszlopa)
        lastNameCol.setMinWidth(130);                                       //minimum szélesség 100 pixel
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());    //itt állítjuk be, hogy az oszlopban minden celle TextField tartalmú legyen
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName")); // itt teszünk bele értékeket PVF paraméter két dolgot vesz át Pojo és milyen értéket vesz ki, paraméterben, hogy milyen néven találja
        
        lastNameCol.setOnEditCommit(                                                //Ha történik változtatás és elküldi (commit)
            new EventHandler<TableColumn.CellEditEvent<Person, String>>() {         //Eseménykezelő. Ha az oszlopban valaki változtatott.
                @Override
                public void handle(TableColumn.CellEditEvent<Person, String> t){    //t esemény
                    ((Person) t.getTableView().getItems().get(                      //táblázatból kérjük az összes elemet és abból
                    t.getTablePosition().getRow())                                  //az adott táblapozícióból az tábla sorát (ahol a módosítás történt)
                    ).setLastName(t.getNewValue());                                 //akkor az objektumnak állítsuk át a lastname-et a paraméterben szereplő kifejezésre
                }
             }
        );
        
        
        TableColumn firstNameCol = new TableColumn("Keresztnév");
        firstNameCol.setMinWidth(125);
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
        
        firstNameCol.setOnEditCommit( 
            new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Person, String> t){
                    ((Person) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    ).setFirstName(t.getNewValue());
                }
             }
        );
        
        
        TableColumn emailCol = new TableColumn("E-mail cím");
        emailCol.setMinWidth(200);
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setCellValueFactory(new PropertyValueFactory<Person, String>("email")); 
        
        emailCol.setOnEditCommit( 
            new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Person, String> t){
                    ((Person) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    ).setEmail(t.getNewValue());
                }
             }
        );
        
        table.getColumns().addAll(lastNameCol, firstNameCol, emailCol);     //add hozzá mindet, amit paraméterbe írtam
        table.setItems(data);                                               //mintaadatok beillesztése
    }
    
    private void setMenuData() {
        TreeItem<String> treeItemRoot1 = new TreeItem<>("Menü");      //Menü fül létrehozása   "ág" ez a fő root
        TreeView<String> treeView = new TreeView<>(treeItemRoot1);    //fa szerkezetben megjelenő menü, aminek átadjuk az "ágat"
        treeView.setShowRoot(false);                                  //Mivel felesleges ez az elágazás nem mutatjuk a programban
        
        TreeItem<String> nodeItemA = new TreeItem<>(MENU_CONTACT);      //
        Node exitNode = new ImageView(new Image(getClass().getResourceAsStream("/exit.png")));
        TreeItem<String> nodeItemB = new TreeItem<>(MENU_EXIT, exitNode);        //
        
        nodeItemA.setExpanded(true);                                   //alapjáraton le van nyitva a menü
        
        Node contactsNode = new ImageView(new Image(getClass().getResourceAsStream("/contacts.png"))); //ImageView, új Imagenek átadjuk az elérési utat
        Node exportNode = new ImageView(new Image(getClass().getResourceAsStream("/export.png")));    //Az Image-t átadjuk az ImageView-nak, ami a Node lesz.
        TreeItem<String> nodeItemA1 = new TreeItem<>(MENU_LIST, contactsNode);             //
        TreeItem<String> nodeItemA2 = new TreeItem<>(MENU_EXPORT, exportNode);        //
        
        nodeItemA.getChildren().addAll(nodeItemA1, nodeItemA2);
        treeItemRoot1.getChildren().addAll(nodeItemA, nodeItemB);      //
        menuPane.getChildren().add(treeView);
        
        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener(){
            public void changed(ObservableValue observable, Object oldValue, Object newValue){
                TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                String selectedMenu;
                selectedMenu = selectedItem.getValue();
                
                if(null != selectedMenu){
                    switch(selectedMenu){
                        case MENU_CONTACT:
                            try {
                                selectedItem.setExpanded(true);
                            } catch (Exception ex) {
                            }
                            break;
                        case MENU_LIST:
                            contactPane.setVisible(true);
                            exportPane.setVisible(false);
                            break;
                        case MENU_EXPORT:
                            contactPane.setVisible(false);
                            exportPane.setVisible(true);
                            break;
                        case MENU_EXIT:
                            System.exit(0);
                            break;
                    }
                }
            }
        });
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setTableData();
        setMenuData();
        
        
    }    

    
    
}
