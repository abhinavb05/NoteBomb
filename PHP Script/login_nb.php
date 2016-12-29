<?php
	$localhost = '166.62.10.144';
	$dbuser = 'abhinavb05';
	$dbpass = 'password';	
	$dbname = 'adminify_wp2';
	$con = mysqli_connect($localhost, $dbuser, $dbpass, $dbname);
	if(!$con){
	echo "error";
	}
	$username = $_POST['username'];
	$password = md5($_POST['password']);
	$response['result'] = array();
	
	$qry = "SELECT * FROM Users_nb WHERE (Email = '$username' AND Password = '$password')";
	$result = mysqli_query($con,$qry);
	$temp = array();
	$temp['username'] = $username;
	$temp['password'] = $password;
    $temp['rowcount'] = mysqli_num_rows($result);
	array_push($response['result'], $temp);
	echo json_encode($response);
	mysqli_close($con);
?>		