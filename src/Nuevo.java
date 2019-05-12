import javafx.application.Application;
import javafx.event.EventType;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javax.imageio.ImageIO;

public class NuevoPaint extends Application{
    private Canvas canvas = new Canvas();
    private GraphicsContext graph = canvas.getGraphicsContext2D();
    private EventType<MouseEvent> mouse;
    private double x, y,posX,posY,rA,rAn;
    private Color color = Color.WHITE;
    private  EventType<MouseEvent> raton;
    
    public static void main(String[] args) {
        // TODO code application logic here
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Prueba Canvas");
        Group root = new Group();
        raton = new  EventType<MouseEvent>();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        BorderPane layout = new BorderPane();
        layout.setCenter(root);

        HBox herramientas = new HBox();
        herramientas.setSpacing(10);
        ColorPicker colorPicker = new ColorPicker();
        RadioButton cb1 = new RadioButton("Lapiz");
        RadioButton cb2 = new RadioButton("Borrador");
        /*RadioButton cb3 = new RadioButton("Rectangulo");
        RadioButton cb4 = new RadioButton("Circulo");
        /*ToggleGroup group = new ToggleGroup();
        cb1.setToggleGroup(group);
        cb2.setToggleGroup(group);
        cb3.setToggleGroup(group);
        cb4.setToggleGroup(group);*/
        //herramientas.getChildren().addAll(cb1,cb2,cb3,cb4,colorPicker);
        layout.setTop(herramientas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        javafx.scene.control.MenuBar menuBar= new javafx.scene.control.MenuBar();
            javafx.scene.control.Menu file= new javafx.scene.control.Menu("Archivo");
                javafx.scene.control.MenuItem itmSave= new javafx.scene.control.MenuItem("Guardar");
                javafx.scene.control.MenuItem itmOpen = new javafx.scene.control.MenuItem("Abrir archivo");
                file.getItems().add(itmSave);
                  
        javafx.scene.control.Menu menu = new javafx.scene.control.Menu("Figuras");
        javafx.scene.control.Menu menuDesRec = new javafx.scene.control.Menu("Rectangulo");
        //La clase ToogleGroup se utiliza comunmente en en los objetos RadioMenuItem para permitir solo seleccionar 1
        ToggleGroup group = new ToggleGroup();
        //Creando los items(RadioMenuItem) que iran en el segundo menu
        RadioMenuItem rbtnRecR = RadioMenuItemBuilder.create()
                .toggleGroup(group)
                .text("Rectangulo Relleno")
                .build();
        RadioMenuItem rbtnRecN = RadioMenuItemBuilder.create()
                .toggleGroup(group)
                .text("Rectangulo")
                .build();
        menuDesRec.getItems().add(rbtnRecR);
        menuDesRec.getItems().add(rbtnRecN);
        menu.getItems().add(menuDesRec);
        
        javafx.scene.control.Menu menuDesCir = new javafx.scene.control.Menu("Circulo");
        //La clase ToogleGroup se utiliza comunmente en en los objetos RadioMenuItem para permitir solo seleccionar 1
        ToggleGroup group2 = new ToggleGroup();
        //Creando los items(RadioMenuItem) que iran en el segundo menu
        RadioMenuItem rbtnCircR = RadioMenuItemBuilder.create()
                .toggleGroup(group2)
                .text("Circulo Relleno")
                .build();
        RadioMenuItem rbtnCircN = RadioMenuItemBuilder.create()
                .toggleGroup(group2)
                .text("Circulo")
                .build();
        menuDesCir.getItems().add(rbtnCircR);
        menuDesCir.getItems().add(rbtnCircN);
        menu.getItems().add(menuDesCir);
        
        menuBar.getMenus().add(file);
        menuBar.getMenus().add(menu);
        
        VBox vB = new VBox(menuBar);
        
        herramientas.getChildren().addAll(vB,cb1,cb2,colorPicker);
        
        Scene scene = new Scene(layout, 500, 500);
        drawCanvas((int) screenSize.getWidth(), (int)screenSize.getWidth(), colorPicker.getValue());
        root.getChildren().add(canvas);
        primaryStage.setScene(scene);
        primaryStage.show();

        colorPicker.setOnAction((e)->{
            color = colorPicker.getValue();
        });
             
            group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                Toggle old_toggle, Toggle new_toggle) {
                    if (group.getSelectedToggle() != null) {
                        rbtnCircN.setSelected(false);
                        rbtnCircR.setSelected(false);
                       if(rbtnRecN.isSelected()){
                           // Detectar ratón pulsado
                        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                    posX = mouseEvent.getX();
                                    posY = mouseEvent.getY();
                            }
                        });

                        // Detectar ratón soltado
                        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                rA = mouseEvent.getX();
                                rAn = mouseEvent.getY();
                                if(rbtnRecN.isSelected()){
                                    drawRect(gc,colorPicker.getValue(),posX,posY,(rA-posX),(rAn-posY));
                                    drawRect(gc,colorPicker.getValue(),posX,rAn,(rA-posX),(posY-rAn));  
                                    drawRect(gc,colorPicker.getValue(),rA,rAn,(posX-rA),(posY-rAn));
                                    drawRect(gc,colorPicker.getValue(),rA,posY,(posX-rA),(rAn-posY));
                                }
                            }
                        });
                       }else if(rbtnRecR.isSelected()){
                           // Detectar ratón pulsado
                        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                    posX = mouseEvent.getX();
                                    posY = mouseEvent.getY();
                            }
                        });

                        // Detectar ratón soltado
                        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                rA = mouseEvent.getX();
                                rAn = mouseEvent.getY();
                                if(rbtnRecR.isSelected()){
                                    drawRectR(gc,colorPicker.getValue(),posX,posY,(rA-posX),(rAn-posY));
                                    drawRectR(gc,colorPicker.getValue(),posX,rAn,(rA-posX),(posY-rAn));  
                                    drawRectR(gc,colorPicker.getValue(),rA,rAn,(posX-rA),(posY-rAn));
                                    drawRectR(gc,colorPicker.getValue(),rA,posY,(posX-rA),(rAn-posY));
                                }
                            }
                        });
                       }
                    }                
                }

        });
        
        group2.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                Toggle old_toggle, Toggle new_toggle) {
                    if (group2.getSelectedToggle() != null) {
                        rbtnRecR.setSelected(false);
                        rbtnRecN.setSelected(false);
                       if(rbtnCircN.isSelected()){
                           scene.setOnMousePressed(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                    posX = mouseEvent.getX();
                                    posY = mouseEvent.getY();
                            }
                        });

                        // Detectar ratón soltado
                        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                rA = mouseEvent.getX();
                                rAn = mouseEvent.getY();
                                if(rbtnCircN.isSelected()){
                                    drawOval(gc,colorPicker.getValue(),posX,posY,(rA-posX),(rAn-posY));
                                    drawOval(gc,colorPicker.getValue(),posX,rAn,(rA-posX),(posY-rAn)); 
                                    drawOval(gc,colorPicker.getValue(),rA,rAn,(posX-rA),(posY-rAn));
                                    drawOval(gc,colorPicker.getValue(),rA,posY,(posX-rA),(rAn-posY));
                                }
                            }
                        });
                       }else if(rbtnCircR.isSelected()){
                           scene.setOnMousePressed(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                    posX = mouseEvent.getX();
                                    posY = mouseEvent.getY();
                            }
                        });

                        // Detectar ratón soltado
                        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                rA = mouseEvent.getX();
                                rAn = mouseEvent.getY();
                                if(rbtnCircR.isSelected()){
                                    drawOvalR(gc,colorPicker.getValue(),posX,posY,(rA-posX),(rAn-posY));
                                    drawOvalR(gc,colorPicker.getValue(),posX,rAn,(rA-posX),(posY-rAn)); 
                                    drawOvalR(gc,colorPicker.getValue(),rA,rAn,(posX-rA),(posY-rAn));
                                    drawOvalR(gc,colorPicker.getValue(),rA,posY,(posX-rA),(rAn-posY));
                                }
                            }
                        });
                       }
                    }                
                }

        });
            

        canvas.setOnMouseReleased(this::mouseUp);
        canvas.setOnMousePressed(this::mouseDown);
        canvas.setOnMouseDragged(this::drawMouse);

    }

    private void drawLine(double xini, double yini, double xfin, double yfin){//Funcion chida para dibujar el trazo

        graph.beginPath();//iniciar trazo
        graph.setLineWidth(3);
        graph.setStroke(color);
        graph.strokeLine(xini,yini,xfin,yfin);
        graph.closePath();//cerrar trazo
    }

    private void drawMouse(MouseEvent e){
        if(mouse == MouseEvent.MOUSE_PRESSED){
            drawLine(x, y, e.getX(), e.getY());
        }
        x = e.getX();
        y = e.getY();
    }

    private void mouseUp(MouseEvent e){
        mouse = MouseEvent.MOUSE_RELEASED;
        x = e.getX();
        y = e.getY();
    }
    private void mouseDown(MouseEvent e){
        mouse = MouseEvent.MOUSE_PRESSED;
        x = e.getX();
        y = e.getY();
    }

    public void drawCanvas(int width, int height, Color color){
        canvas.setWidth(width);
        canvas.setHeight(height);
        graph.setFill(color);
        graph.setStroke(color);
    }
    
    //Figuras 
    public void drawRect(GraphicsContext gc,Color color,double posx,double posy,double ra, double ran){
        
        gc.setLineWidth(2.0);
        // Set fill color
        gc.setStroke(color);
        // Draw a rounded Rectangl
        gc.strokeRect(posx, posy, ra, ran);
    }
     public void drawRectR(GraphicsContext gc,Color color,double posx,double posy,double ra, double ran){
         gc.setLineWidth(2.0);
        // Set fill color
        gc.setFill(color);
        // Draw a rounded Rectangl
        gc.fillRect(posx, posy, ra, ran);
     }
     public void drawOval(GraphicsContext gc,Color color,double posx,double posy,double ra, double ran){
        gc.setLineWidth(2.0);
        // Set fill color
        gc.setStroke(color);
        gc.strokeOval(posx, posy, ra, ran);
     }
     public void drawOvalR(GraphicsContext gc,Color color,double posx,double posy,double ra, double ran){
        gc.setLineWidth(2.0);
        // Set fill color
        gc.setFill(color);
        gc.fillOval(posx, posy, ra, ran);
     }
     public void guardar(){
        try{
            WritableImage snapshot = canvas.snapshot(null, null);
            
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "jpg", new File("paint.jpg"));
        }catch(Exception e){
            System.out.println("No se pudo guardar la imagen "+ e);
        }
    }
}
