# Image processing API 
This repository contain a simple API that makes a basic image processing
# Features
- Convert an image to black and white
- Resize an image 
# Use
Let's see the step for using it locally
- **Step 1:** Clone this repository in your device
- **Step 2:** Go to the `src/main/resources` and you can see the file **application.properties**. Open it and modify the line to specify the directory for saving your image:
```shell
image.directory = absolute/path/to/your/file
```
- **Step 3:** Always in the resources package, you can see a directory called `templates`, so look inside. There is a JSON file collection that you can already use and import for testing the endpoint on [Postman](https://www.postman.com/downloads/)
- **Step 4:** Run the Springboot application and enjoy it!
# openAPI specification
Since it is  an API, here's the link for the specification: [Image processing API](https://petstore.swagger.io/?url=https://raw.githubusercontent.com/Harenabs21/image-processing-api/main/docs/api.yml#/)