<div class="container">
    <?php echo $server_response; ?>
    <form method="post" role="form">
                    <input class="image-name" type="hidden" name="image-name" value="<?php echo $pick_name ?>"/>
                    <div id="file-upload" class="form-group">
                        <button type="button" style="display: none" class="browse btn btn-lg btn-primary btn-block" onclick="document.getElementById('imageUpload').click()">Add Image</button>
                        <div  class="progress" style="display: none">
                            <div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="45" aria-valuemin="0" aria-valuemax="100" style="width: 0%"></div>
                        </div>
                        <div class="image-preview">
                            <a href="javascript:void(0);" class="thumbnail">
                                <img src="<?php echo $pick_url ?>" alt="image">
                            </a>
                            <button type="button" class="btn btn-danger btn-block">Remove Image</button>
                        </div>
                        <input id="imageUpload" style="display: none" type="file" class="form-control" name="files" accept="image/*" />
                    </div>
                    
                    <div class="row">
                        <div class="col-sm-6">
                            <div class="form-group">
                                <div class="input-group">
                                    <input type="date" class="form-control" value="<?php echo $pick_date; ?>" name="pick-date" required>
                                    <div class="input-group-addon">Publish Date</div>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <div class="input-group">
                                    <input type="time" class="form-control" value="<?php echo $pick_time ?>" name="pick-time" required>
                                    <div class="input-group-addon">Publish Time</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <textarea class="form-control" name="pick-description" placeholder="Image description" rows="5" required><?php echo $pick_description ?></textarea>
                    </div>
                    <input type="submit" name="submit" class="btn btn-primary" value="Submit">
                </form>
</div>