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
	$query = "SELECT * FROM user_profile_table WHERE BINARY login_id = '$loginid'
			AND BINARY password = '$password'";
	$result = mysqli_query($conn,$query);
	if($result->num_rows > 0) {
		echo "success! User data found in database!!!";
	} else {
		echo "Incorrect username or password :(";
	}
}
$conn->close();
exit;
?>


