<?php
define('HOST','166.62.10.144');
define('USER','abhinavb05');
define('PASS','password');
define('DB','adminify_wp2');
$con = mysqli_connect(HOST,USER,PASS,DB);
if(!$con){
echo "error";
}
$n = $_POST['name'];
$m = $_POST['email'];
$p = md5($_POST['password']);

$sql = "INSERT INTO Users_nb (Name,Email,Password) VALUES ('$n','$m','$p');";
$result = mysqli_query($con,$sql);
mysqli_close($con);
?>
