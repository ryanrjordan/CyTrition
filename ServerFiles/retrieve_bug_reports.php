<?php
$servername = "mysql.cs.iastate.edu";
$username = "dbu309yt8";
$password = "TarDx@ew";
$dbname = "db309yt8";
$conn = mysqli_connect($servername,$username,$password,$dbname);
if(!$conn) {
	die("Connection failed: " . mysqli_connect_error());
}
//login_id not used
if(isset($_POST['login_id'])) {
	$query = "SELECT * FROM bug_reports;";
	$result = mysqli_query($conn,$query);
	if($result->num_rows > 0) {
		while($row =  mysqli_fetch_row($result)) {
			echo "Report," . $row[0] . "," . $row[1] . "," . $row[2] . "-";
		}
	} else {
		echo "Can't find any reports.";
	}
}

$conn->close();
exit;
?>