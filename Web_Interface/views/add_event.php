<?php

    require './database/Database.php';
    
    $server_response = '';
    
    if( isset( $_POST['submit'] ) ) {
        
        $db = new Database();
        
        $event_name =   addslashes( $_POST['event-name'] ) ;
        
        $event_location = addslashes(  $_POST['event-location'] );
        
        $postDateTime = $_POST['event-date'].' '.$_POST['event-time'];
        
        $datetime = DateTime::createFromFormat( 'Y-m-d H:i',  $postDateTime , new DateTimeZone( 'UTC' ) );
        $event_datetime = $datetime->format( 'Y-m-d H:i:s' );
        
        $event_description =  addslashes(  $_POST['event-description'] ) ;
        $longitude = htmlentities( $_POST['location-longitude'] );
        $latitude = htmlentities( $_POST['location-latitude'] );
        
        $update_type = 1;
        
        $insertQuery = "INSERT INTO `message`(`datetime`, `update_type`, `name`, `description`, `location`, `longitude`, `latitude`) VALUES ('{$event_datetime}','{$update_type}','{$event_name}','{$event_description}','{$event_location}','{$longitude}','{$latitude}')";
        
        if( $db->queryInsertDb($insertQuery) ) {
            $server_response = '<div class="alert alert-success" role="alert">Event '.$event_name.' has been <strong>successfully</strong> added</div>';
        } else {
            $server_response = '<div class="alert alert-danger" role="alert">Something went wrong!!! Thats all we know</div>';
        }
        
    }
    
?>
<div class="container">
    <?php echo $server_response; ?>
    <form id="form" method="post" action="<?php echo BASE_URL.'add-event.php' ?>" role="form">
                    
                    <div class="form-group">
                        <input type="text" class="form-control" name="event-name" placeholder="Event Name" required/>
                    </div>
                    <div class="form-group">
                        <input type="text" class="form-control" name="event-location" placeholder="Event location" required>
                    </div>
                    <div class="row">
                        <div class="col-sm-6">
                            <div class="form-group">
                                <input type="text" placeholder="Longitude" class="form-control" name="location-longitude" required> 
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <input type="text" placeholder="Latitude" class="form-control" name="location-latitude" required> 
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-6">
                            <div class="form-group">
                                <div class="input-group">
                                    <input type="date" class="form-control" name="event-date" required>
                                    <div class="input-group-addon">Publish Date</div>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <div class="input-group">
                                    <input type="time" class="form-control" name="event-time" required>
                                    <div class="input-group-addon">Publish Time</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                      <textarea class="form-control" name="event-description" placeholder="Event description" rows="5" required></textarea>
                    </div>

                    <input type="submit" name="submit" class="btn btn-primary" value="Submit">
                </form>
</div>
<script>
    
</script>