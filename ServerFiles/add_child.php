<?php
$servername = "mysql.cs.iastate.edu";
$username = "dbu309yt8";
$password = "TarDx@ew";
$dbname = "db309yt8";
$conn = mysqli_connect($servername,$username,$password,$dbname);

if(!$conn) {
	die("Connection failed: " . mysqli_connect_error());
}

if(isset($_POST['parent_id']) && isset($_POST['child_id'])) {
	$parentid = $_POST['parent_id'];
	$childid = $_POST['child_id'];
	
	$check = "SELECT * FROM user_profile_table WHERE BINARY login_id = '$childid';";
	$result = mysqli_query($conn,$check);
	if($result->num_rows > 0) {
		$query = "INSERT INTO parent_children_table (parent_id,child_id) VALUES ('$parentid', '$childid');";
		if($conn->query($query)===TRUE) {
			echo "successful";
		} else {
			echo "Couldn't add to database";
		}
	} else {
		echo "Child's name didn't appear in database";
	}
	
}

$conn->close();
exit;
?>
