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
	
	$query = "DELETE FROM parent_children_table WHERE parent_id='$parentid' AND child_id='$childid';";
	if($conn->query($query)===TRUE) {
		echo "successful";
	} else {
		echo "Couldn't add to database";
	}
	
}

$conn->close();
exit;
?>
