<?php
$servername = "mysql.cs.iastate.edu";
$username = "dbu309yt8";
$password = "TarDx@ew";
$dbname = "db309yt8";
$conn = mysqli_connect($servername,$username,$password,$dbname);

if(!$conn) {
	die("Connection failed: " . mysqli_connect_error());
}

if(isset($_POST['login_id']) && isset($_POST['recipe_name'])) {
	$login_id = $_POST['login_id'];
	$recipe_name = $_POST['recipe_name'];

	//Get recipe_id
	$query2 = "SELECT recipe_id FROM Recipe WHERE login_id = '$login_id' AND recipe_name = '$recipe_name';";
	$result2 = mysqli_query($conn,$query2);
	if ($result2->num_rows > 0) {
		$row2 =  mysqli_fetch_row($result2);
		$recipe_id = $row2[0];

		//Delete all ingredients from the Recipe
		$query3 = "DELETE FROM RecipeIngredient WHERE recipe_id='$recipe_id';";
		if($conn->query($query3)===TRUE) {

			//Delete the Recipe
			$query4 = "DELETE FROM Recipe WHERE recipe_id='$recipe_id' AND login_id='$login_id';";
			if($conn->query($query4)===TRUE) {
				echo "yes";
			}
		}
	}
}

$conn->close();
exit;
?>
