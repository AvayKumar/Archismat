<?php

    require './database/Database.php';
    
    $server_response = '';
    
    if( isset( $_POST['submit'] ) ) {
        
        $db = new Database();
        
        $image_name = htmlentities( $_POST['image-name'] ) ;
        
        $postDateTime = $_POST['pick-date'].' '.$_POST['pick-time'];
        
        $datetime = DateTime::createFromFormat( 'Y-m-d H:i',  $postDateTime , new DateTimeZone( 'UTC' ) );
        $pick_datetime = $datetime->format( 'Y-m-d H:i:s' );
        
        $pick_description = addslashes( $_POST['pick-description'] );

        $update_type = 2;
                
        $insertQuery = "INSERT INTO `message`(`datetime`, `update_type`, `description`, `pick`) VALUES ('{$pick_datetime}','{$update_type}','{$pick_description}','{$image_name}')";
        
        if( $db->queryInsertDb($insertQuery) ) {
            $server_response = '<div class="alert alert-success" role="alert">Image  update has been <strong>successfully</strong> added</div>';
        } else {
            echo mysqli_error(Database::$conn);
            $server_response = '<div class="alert alert-danger" role="alert">Something went wrong!!! Thats all we know</div>';
        }
        
    }
    
?>
<div class="container">
    <?php echo $server_response; ?>
    <form method="post" action="<?php echo BASE_URL.'add-picture.php' ?>" role="form">
                    
                    <div id="file-upload" class="form-group">
                        <button type="button" class="browse btn btn-lg btn-primary btn-block" onclick="document.getElementById('imageUpload').click()">Add Image</button>
                        <div  class="progress" style="display: none">
                            <div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="45" aria-valuemin="0" aria-valuemax="100" style="width: 0%"></div>
                        </div>
                        <div class="image-preview" style="display: none">
                            <a href="javascript:void(0);" class="thumbnail">
                                <img src="" alt="image">
                            </a>
                            <button type="button" class="btn btn-danger btn-block">Remove Image</button>
                        </div>
                        <input id="imageUpload" style="display: none" type="file" class="form-control" name="files" accept="image/*" />
                    </div>
                    
                    <div class="row">
                        <div class="col-sm-6">
                            <div class="form-group">
                                <div class="input-group">
                                    <input type="date" class="form-control" name="pick-date" required>
                                    <div class="input-group-addon">Publish Date</div>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <div class="input-group">
                                    <input type="time" class="form-control" name="pick-time" required>
                                    <div class="input-group-addon">Publish Time</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <textarea class="form-control" name="pick-description" placeholder="Image description" rows="5" required></textarea>
                    </div>
                    <input type="submit" name="submit" class="btn btn-primary" value="Submit">
                </form>
</div>