<div class="container">
    <?php echo $server_response; ?>
    <form id="form" method="post" role="form">
                    
                    <div class="form-group">
                        <input type="text" class="form-control" name="event-name" value="<?php echo $event_name; ?>" placeholder="Event Name" required/>
                    </div>
                    <div class="form-group">
                        <input type="text" class="form-control" name="event-location" value="<?php echo $event_location; ?>" placeholder="Event location" required>
                    </div>
                    <div class="row">
                        <div class="col-sm-6">
                            <div class="form-group">
                                <input type="text" value="<?php echo $event_longitude; ?>" placeholder="Longitude" class="form-control" name="location-longitude" required> 
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <input type="text" value="<?php echo $event_latitude; ?>" placeholder="Latitude" class="form-control" name="location-latitude" required> 
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-6">
                            <div class="form-group">
                                <div class="input-group">
                                    <input type="date" class="form-control" value="<?php echo $event_date; ?>" name="event-date" required>
                                    <div class="input-group-addon">Publish Date</div>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <div class="input-group">
                                    <input type="time" class="form-control" value="<?php echo $event_time; ?>" name="event-time" required>
                                    <div class="input-group-addon">Publish Time</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                      <textarea class="form-control" name="event-description" placeholder="Event description" rows="5" required><?php echo $event_description; ?></textarea>
                    </div>

                    <input type="submit" name="submit" class="btn btn-primary" value="Submit">
                </form>
</div>
<script>
    
</script>