package com.example.fxmlplayer;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;


public class Controller  {
    @FXML
    private Label timeDuration = new Label();

    @FXML
    private MediaView mediaView;
    private MediaPlayer player;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Slider progressBar;
    @FXML
    private ImageView muteBtn;
    @FXML
    private ImageView playBtn;


    ImageView imageView1 = new ImageView("play.png");
    ImageView imageView2 = new ImageView("pause.png");
    ImageView imageView3 = new ImageView("stop.png");
    ImageView imageView4 = new ImageView("backward.png");
    ImageView imageView5 = new ImageView("forward.png");
    ImageView imageView6 = new ImageView("mute.png");

    @FXML
    public void initialize() {
        String video = getClass().getResource("ManCity.mp4").toExternalForm();
        Media media = new Media(video);
        player = new MediaPlayer(media);
        mediaView.setMediaPlayer(player);

        timeDurationLabel();

        String muteBtn1 = getClass().getResource("/sound.png").toExternalForm();
        Image image = new Image(muteBtn1);
        muteBtn.setImage(image);

        String playbtn1 = getClass().getResource("/play.png").toExternalForm();
        Image image1 = new Image(playbtn1);
        playBtn.setImage(image1);



        volumeSlider.setValue(player.getVolume()*100);
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                player.setVolume(volumeSlider.getValue()/100);
            }
        });


        player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration oldValue, Duration newValue) {
                progressBar.setValue(newValue.toSeconds());
            }
        });
        progressBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                player.seek(Duration.seconds(progressBar.getValue()));
            }
        });

        progressBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                player.seek(Duration.seconds(progressBar.getValue()));
            }
        });

        player.setOnReady(new Runnable() {
            @Override
            public void run() {
                Duration total=media.getDuration();
                progressBar.setMax(total.toSeconds());
            }
        });

    }

    @FXML
    void playVideo(MouseEvent event ) {
       if(player.getStatus() != MediaPlayer.Status.PLAYING){
           player.play();
           String playbtn1 = getClass().getResource("/pause.png").toExternalForm();
           Image image1 = new Image(playbtn1);
           playBtn.setImage(image1);
       }else {
           player.pause();
           String playbtn1 = getClass().getResource("/play.png").toExternalForm();
           Image image1 = new Image(playbtn1);
           playBtn.setImage(image1);
       }

    }

    @FXML
    void stopVideo(MouseEvent event) {
        player.stop();
    }

    @FXML
    void nextBtnClicked(MouseEvent event) {
        player.seek(player.getTotalDuration());
        player.play();
    }

    @FXML
    void nextBttnClicked(MouseEvent event) {
        player.seek(player.getStartTime());
        player.play();
    }
    @FXML
    void volumeClicked(MouseEvent event) {

    }
    @FXML
    void muteButtonClicked(MouseEvent event) {
        if(player.isMute() == true){
            String muteBtn1 = getClass().getResource("/sound.png").toExternalForm();
            player.setMute(false);
            Image image = new Image(muteBtn1);
            muteBtn.setImage(image);
        }else if(player.isMute() == false){
            player.setMute(true);
            String muteBtn2 = getClass().getResource("/mute.png").toExternalForm();
            Image image = new Image(muteBtn2);
            muteBtn.setImage(image);
        }

    }
    public void timeDurationLabel(){
        timeDuration.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getTime(player.getCurrentTime()) ;
            }
        },player.currentTimeProperty()));
    }

    public String getTime(Duration time){
        int hours = (int) time.toHours();
        int minutes = (int) time.toMinutes();
        int seconds = (int) time.toSeconds();

        if (seconds > 59) seconds = seconds % 60;
        if (minutes > 59) minutes = minutes % 60;
        if (hours > 59) hours = hours % 60;

        if(hours > 0 ) return String.format("%d:%02d%02d",
                hours, minutes, seconds);

        else return String.format("%02d:%02d",
                minutes,seconds);


    }



}
