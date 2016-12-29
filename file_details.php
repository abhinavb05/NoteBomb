<?php 
 
	$title  = urldecode($_POST['title']);
	$descrip   = urldecode($_POST['description']);
	$path = urldecode($_POST['path']);
	$servername = "localhost";
	$username = "abhinavb05";
	$password = "password";
	$dbname = "adminify_wp2";
	$conn = new mysqli($servername, $username, $password, $dbname);
	
	if ($conn->connect_error) {
		die("Connection failed: " . $conn->connect_error);
	} 

	$sql = "INSERT INTO Notes (Id,Title,Description,Path)
	VALUES (UUID(),'$title','$descrip','$path')";

	if ($conn->query($sql) === TRUE) {
		echo "New record created successfully";
	} else {
		echo "Error: " . $sql . "<br>" . $conn->error;
	}

	$conn->close();
?>
