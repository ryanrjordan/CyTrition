<?php
$servername = "mysql.cs.iastate.edu";
$username = "dbu309yt8";
$password = "TarDx@ew";
$dbname = "db309yt8";
$conn = mysqli_connect($servername,$username,$password,$dbname);
if(!$conn) {
	die("Connection failed: " . mysqli_connect_error());
}


if(isset($_POST['login_id'])) {
	$loginid = $_POST['login_id'];
	$password = $_POST['password'];
	$email = $_POST['email'];
	$name = $_POST['name'];
	$gender = $_POST['gender'];
	$height = $_POST['height'];
	$weight = $_POST['weight'];
	$user_type = $_POST['user_type'];
	$age = $_POST['age'];
	$query = "UPDATE user_profile_table SET password='$password', email='$email', name='$name', gender='$gender', height='$height', weight='$weight', user_type='$user_type', age='$age' WHERE login_id='$loginid'";
	if($conn->query($query)===TRUE) {
		echo "successful user data update!";
	} else {
		echo "Couldn't update user data at PHP->MySQL stage.";
	}
}

$conn->close();
exit;
?>