<?php
$servername = "mysql.cs.iastate.edu";
$username = "dbu309yt8";
$password = "TarDx@ew";
$dbname = "db309yt8";
$conn = mysqli_connect($servername,$username,$password,$dbname);
if(!$conn) {
	die("Connection failed: " . mysqli_connect_error());
}


if(isset($_POST['login_id'])&&isset($_POST['password'])) {
	$loginid = $_POST['login_id'];
	$password = $_POST['password'];
	$query = "INSERT INTO user_profile_table (login_id,password,user_type) VALUES ('$loginid','$password','0')";
	if($conn->query($query)===TRUE) {
		echo "successful user account creation!";
	} else {
		echo "Could't insert user data at PHP->MySQL stage.";
	}
}


$conn->close();
exit;
?>