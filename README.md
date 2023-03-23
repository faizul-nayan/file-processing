<div>
  <h2>Deployment Documentation</h2>
  <p>This documentation will guide you through the process of deploying the Spring Boot project using Docker.</p>

<h3>Prerequisites</h3>
  <p>To deploy the project, you will need the following:</p>
  <ul>
    <li>Docker installed on your local machine or the server you want to deploy to.</li>
    <li>Java 11 installed on your local machine (if you want to build the project locally).</li>
  </ul>

<h3>Run the Docker container</h3>
<ol>
<li>Start the MySQL container using the following command:</li>
<pre><code>docker-compose up -d db
</code></pre>
This will start the MySQL container in the background.
</ol>

<h3>Building the Docker Image</h3>
  <p>To build the Docker image, follow these steps:</p>
  <ol>
    <li>Clone the project to your local machine.</li>
    <li>Navigate to the root directory of the project.</li>
    <li>Open a terminal window in the root directory.</li>
    <li>Run the following command to build the Docker image:</li>
  </ol>
  <pre><code>docker build -t file-processing .</code></pre>

<h3>Running the Docker Container</h3>
  <p>To run the Docker container, follow these steps:</p>
  <ol>
    <li>Run the following command to start the Docker container:</li>
  </ol>
  <pre><code>docker run -p 9008:9008 --name file-processing --link db:mysql -d file-processing
</code></pre>
  <p>This command will start the Docker container and map the container's port 9008 to your local machine's port 9008.</p>

<h3>Using the Application</h3>
  <p>To use the application, open a web browser and navigate to <a href="http://localhost:9008">http://localhost:9008</a>. This will display the home page of the application.</p>

<h3>Stopping the Docker Container</h3>
  <p>To stop the Docker container, follow these steps:</p>
  <ol>
    <li>Open a new terminal window.</li>
    <li>Run the following command to list the running Docker containers:</li>
  </ol>
  <pre><code>docker ps</code></pre>
  <p>This command will display a list of running Docker containers along with their container IDs.</p>
  <ol start="3">
    <li>Find the container ID for the file-processing container in the list.</li>
    <li>Run the following command to stop the Docker container:</li>
  </ol>
  <pre><code>docker stop &lt;container_id&gt;</code></pre>
  <p>Replace &lt;container_id&gt; with the container ID you found in step 3.</p>

<h3>Conclusion</h3>
  <p>This concludes the deployment documentation for the Spring Boot project using Docker. If you have any issues or questions, please feel free to reach out to the project's maintainers.</p>
</div>
