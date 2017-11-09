<?php

/**
 * Class to handle all db operations
 * This class will have CRUD methods for database tables
 *
 * @author Ravi Tamada
 * @link URL Tutorial link
 */
class DbHandler {

    private $conn;

    function __construct() {
        require_once dirname(__FILE__) . '/DbConnect.php';
        // opening db connection
        $db = new DbConnect();
        $this->conn = $db->connect();
    }

    /* ------------- `users` table method ------------------ */

    /**
     * Creating new user
     * @param String $name User full name
     * @param String $email User login email id
     * @param String $password User login password
     */
    public function createUser($name, $username, $email, $password) {
        require_once 'PassHash.php';
        $response = array();

        // First check if user already existed in db
        if ($this->isUserExists($email)) {
            // Failed to create user
            return USER_ALREADY_EXISTED;
        }

         // First check if user already existed in db
        if ($this->isUsernameExists($username)) {
            // Failed to create user
            return USERNAME_ALREADY_EXISTED;
        }

        // Generating password hash
        $password_hash = PassHash::hash($password);

        // Generating API key
        $apikey = $this->generateApiKey();

        // insert query
        $stmt = $this->conn->prepare("INSERT INTO users(name, username, email, password_hash, apikey, curlevel, maxlevel, status) values(?, ?, ?, ?, ?, 4, 5, 1)");
        $stmt->bind_param("sssss", $name, $username, $email, $password_hash, $apikey);

        $result = $stmt->execute();

        $stmt->close();

        // Check for successful insertion
        if ($result) {
            // User successfully inserted
            return USER_CREATED_SUCCESSFULLY;
        } else {
            // Failed to create user
            return USER_CREATE_FAILED;
        }

        return USER_CREATE_ERROR;
    }

    /**
     * Checking user login
     * @param String $email User login email id
     * @param String $password User login password
     * @return boolean User login status success/fail
     */
    public function checkLogin($email, $password) {
        // fetching user by email
        $stmt = $this->conn->prepare("SELECT password_hash FROM users WHERE email = ?");

        $stmt->bind_param("s", $email);

        $stmt->execute();

        $stmt->bind_result($password_hash);

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // Found user with the email
            // Now verify the password

            $stmt->fetch();

            $stmt->close();

            if (PassHash::check_password($password_hash, $password)) {
                // User password is correct
                return TRUE;
            } else {
                // user password is incorrect
                return FALSE;
            }
        } else {
            $stmt->close();

            // user not existed with the email
            return FALSE;
        }
    }

    /**
         * Checking for duplicate user by email address
         * @param String $email email to check in db
         * @return boolean
         */
        private function isUserExists($email) {
            $stmt = $this->conn->prepare("SELECT id from users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $stmt->store_result();
            $num_rows = $stmt->num_rows;
            $stmt->close();
            return $num_rows > 0;
        }

    /**
     * Checking for duplicate user by email address
     * @param String $email email to check in db
     * @return boolean
     */
    private function isUsernameExists($username) {
        $stmt = $this->conn->prepare("SELECT id from users WHERE username = ?");
        $stmt->bind_param("s", $username);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }

    /**
     * Fetching user by email
     * @param String $email User email id
     */
    public function getUserByEmail($email) {
        $stmt = $this->conn->prepare("SELECT name, username, email, curlevel, maxlevel, apikey, status, created_at FROM users WHERE email = ?");
        $stmt->bind_param("s", $email);
        if ($stmt->execute()) {
            // $user = $stmt->get_result()->fetch_assoc();
            $stmt->bind_result($name, $username, $email, $curlevel, $maxlevel, $apikey, $status, $created_at);
            $stmt->fetch();
            $user = array();
            $user["name"] = $name;
            $user["username"] = $username;
            $user["email"] = $email;
            $user["curlevel"] = $curlevel;
            $user["maxlevel"] = $maxlevel;
            $user["apikey"] = $apikey;
            $user["status"] = $status;
            $user["created_at"] = $created_at;
            $stmt->close();
            return $user;
        } else {
            return NULL;
        }
    }

    /**
     * Fetching user api key
     * @param String $user_id user id primary key in user table
     */
    public function getApiKeyById($user_id) {
        $stmt = $this->conn->prepare("SELECT apikey FROM users WHERE id = ?");
        $stmt->bind_param("i", $user_id);
        if ($stmt->execute()) {
            // $apikey = $stmt->get_result()->fetch_assoc();
            // TODO
            $stmt->bind_result($apikey);
            $stmt->close();
            return $apikey;
        } else {
            return NULL;
        }
    }

    /**
     * Fetching user id by api key
     * @param String $api_key user api key
     */
    public function getUserId($apikey) {
        $stmt = $this->conn->prepare("SELECT id FROM users WHERE apikey = ?");
        $stmt->bind_param("s", $apikey);
        if ($stmt->execute()) {
            $stmt->bind_result($user_id);
            $stmt->fetch();
            // TODO
            // $user_id = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user_id;
        } else {
            return NULL;
        }
    }

    /**
     * Validating user api key
     * If the api key is there in db, it is a valid key
     * @param String $apikey user api key
     * @return boolean
     */
    public function isValidApiKey($apikey) {
        $stmt = $this->conn->prepare("SELECT id from users WHERE apikey = ?");
        $stmt->bind_param("s", $apikey);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }

    /**
     * Generating random Unique MD5 String for user Api key
     */
    private function generateApiKey() {
        return md5(uniqid(rand(), true));
    }

    /* ------------- `tasks` table method ------------------ */

    /**
     * Creating new task
     * @param String $user_id user id to whom task belongs to
     * @param String $task task text
     */
    public function createTask($user_id, $task) {
        $stmt = $this->conn->prepare("INSERT INTO tasks(task) VALUES(?)");
        $stmt->bind_param("s", $task);
        $result = $stmt->execute();
        $stmt->close();

        if ($result) {
            // task row created
            // now assign the task to user
            $new_task_id = $this->conn->insert_id;
            $res = $this->createUserTask($user_id, $new_task_id);
            if ($res) {
                // task created successfully
                return $new_task_id;
            } else {
                // task failed to create
                return NULL;
            }
        } else {
            // task failed to create
            return NULL;
        }
    }



    /* ------------- `score` table method ------------------ */

    /**
     * Creating new score
     * @param String $user_id user id to whom task belongs to
     * @param String $task task text
     */
    public function createScore($user_id, $nscore, $nscore1, $nscore2, $game, $level) {

        // Get the username based on the user_id
        $stmt = $this->conn->prepare("SELECT username FROM users WHERE id = ?");

        $stmt->bind_param("i", $user_id);
        if ($stmt->execute()) {
            $stmt->bind_result($username);
            $stmt->fetch();
            $stmt->close();
        } else {
            return NULL;
        }

        $maxlevel = 0;

        // Get the username based on the user_id
        $stmt = $this->conn->prepare("SELECT maxlevel FROM users WHERE id = ?");

        $stmt->bind_param("i", $user_id);
        if ($stmt->execute()) {
            $stmt->bind_result($maxlevel);
            $stmt->fetch();
            $stmt->close();
        } else {
            return NULL;
        }

        if ($level == $maxlevel) {
            if ($maxlevel < 10)
            {
                $maxlevel = $level + 1;
            } else {
                $maxlevel = 10;
            }

        }

        $stmt = $this->conn->prepare("UPDATE users SET maxlevel = ?, curlevel = ? WHERE id = ?");
        $stmt->bind_param("iii", $maxlevel, $level, $user_id);
        if ($stmt->execute()) {
            $stmt->fetch();
            $stmt->close();
        } else {
            return NULL;
        }


        // Check if this is a new high score
        $status = 0;
        $stmt = $this->conn->prepare("SELECT MIN(score) FROM `highscore` WHERE game = ?");
        $stmt->bind_param("i", $game);

        if ($stmt->execute()) {
            $stmt->bind_result($score);
            $stmt->fetch();
            $stmt->close();
        } else {
            return NULL;
        }

        // Characterize the type of score 2 New High Score, 1 Tied, 0 Nothing special
        if ($nscore < $score) {
            $status = 2;
        } else if ($nscore == $score) {
            $status = 1;
        } else {
            $status = 0;
        }


        $stmt = $this->conn->prepare("INSERT INTO highscore(user, username, score, score1, score2, status, game, level) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
        $stmt->bind_param("isiiiiii", $user_id, $username, $nscore, $nscore1, $nscore2, $status, $game, $level);
        $result = $stmt->execute();
        if (false === $result) {
            return NULL;
        }
        $id = $this->conn->insert_id;
        $stmt->close();


        // Rank the new score
        $stmt = $this->conn->prepare("SELECT id,username,score, FIND_IN_SET(score,(SELECT GROUP_CONCAT(score ORDER BY score ASC)FROM highscore WHERE game = ? AND level = ?)) AS rank FROM highscore WHERE game = ? AND level = ? AND id = ?");

        $stmt->bind_param("iiiii", $game, $level, $game, $level, $id );
        if ($stmt->execute()) {
            $stmt->bind_result($id, $username, $score, $rank);
            $stmt->fetch();
            $stmt->close();
        } else {
            return NULL;
      }

      $res = array();
      $res["maxlevel"] = $maxlevel;
      $res["curlevel"] = $level;
      $res["username"] = $username;
      $res["status"] = $status;
      $res["rank"] = $rank;
      return $res;
    }





    /* ------------- `score` table method ------------------ */

    /**
    * Creating new score
    * @param String $user_id user id to whom task belongs to
    * @param String $task task text
    */
    public function getHighScore($game, $level, $user) {
         if ($user > 0) {
            $stmt = $this->conn->prepare("SELECT username,score,level, FIND_IN_SET(score,(SELECT GROUP_CONCAT(score ORDER BY score ASC)FROM highscore WHERE game = ? AND level = ? AND user = ? )) AS rank FROM highscore WHERE game = ? AND level = ? AND user = ? ORDER BY rank LIMIT 10 ");
            $stmt->bind_param("iiiiii", $game, $level, $user, $game, $level, $user);
         } else {
            $stmt = $this->conn->prepare("SELECT username,score,level, FIND_IN_SET(score,(SELECT GROUP_CONCAT(score ORDER BY score ASC)FROM highscore WHERE game = ? AND level = ? )) AS rank FROM highscore WHERE game = ? AND level = ? ORDER BY rank LIMIT 10 ");
            $stmt->bind_param("iiii", $game, $level, $game, $level);
         }

         $stmt->execute();

         /* Store the result (to get properties) */
         $stmt->store_result();

         /* Get the number of rows */
         $num_of_rows = $stmt->num_rows;


         $arr = array();

         /* Bind the result to variables */
         $stmt->bind_result($username, $score, $rank, $level);

         $highscore = "";
         while ( $stmt->fetch() ) {
             $highscore .= $username . " " . $score . ";";
         }
         if ($highscore == NULL) {
             $highscore = "System 999999";
         }
         return $highscore;
         }

    /**
     * Fetching single task
     * @param String $task_id id of the task
     */
    public function getTask($task_id, $user_id) {
        $stmt = $this->conn->prepare("SELECT t.id, t.task, t.status, t.created_at from tasks t, user_tasks ut WHERE t.id = ? AND ut.task_id = t.id AND ut.user_id = ?");
        $stmt->bind_param("ii", $task_id, $user_id);
        if ($stmt->execute()) {
            $res = array();
            $stmt->bind_result($id, $task, $status, $created_at);
            // TODO
            // $task = $stmt->get_result()->fetch_assoc();
            $stmt->fetch();
            $res["id"] = $id;
            $res["task"] = $task;
            $res["status"] = $status;
            $res["created_at"] = $created_at;
            $stmt->close();
            return $res;
        } else {
            return NULL;
        }
    }

    /**
     * Fetching all user tasks
     * @param String $user_id id of the user
     */
    public function getAllUserTasks($user_id) {
        $stmt = $this->conn->prepare("SELECT t.* FROM tasks t, user_tasks ut WHERE t.id = ut.task_id AND ut.user_id = ?");
        $stmt->bind_param("i", $user_id);
        $stmt->execute();
        $tasks = $stmt->get_result();
        $stmt->close();
        return $tasks;
    }

    /**
     * Updating task
     * @param String $task_id id of the task
     * @param String $task task text
     * @param String $status task status
     */
    public function updateTask($user_id, $task_id, $task, $status) {
        $stmt = $this->conn->prepare("UPDATE tasks t, user_tasks ut set t.task = ?, t.status = ? WHERE t.id = ? AND t.id = ut.task_id AND ut.user_id = ?");
        $stmt->bind_param("siii", $task, $status, $task_id, $user_id);
        $stmt->execute();
        $num_affected_rows = $stmt->affected_rows;
        $stmt->close();
        return $num_affected_rows > 0;
    }

    /**
     * Deleting a task
     * @param String $task_id id of the task to delete
     */
    public function deleteTask($user_id, $task_id) {
        $stmt = $this->conn->prepare("DELETE t FROM tasks t, user_tasks ut WHERE t.id = ? AND ut.task_id = t.id AND ut.user_id = ?");
        $stmt->bind_param("ii", $task_id, $user_id);
        $stmt->execute();
        $num_affected_rows = $stmt->affected_rows;
        $stmt->close();
        return $num_affected_rows > 0;
    }

    /* ------------- `user_tasks` table method ------------------ */

    /**
     * Function to assign a task to user
     * @param String $user_id id of the user
     * @param String $task_id id of the task
     */
    public function createUserTask($user_id, $task_id) {
        $stmt = $this->conn->prepare("INSERT INTO user_tasks(user_id, task_id) values(?, ?)");
        $stmt->bind_param("ii", $user_id, $task_id);
        $result = $stmt->execute();

        if (false === $result) {
            die('execute() failed: ' . htmlspecialchars($stmt->error));
        }
        $stmt->close();
        return $result;
    }

}

?>
