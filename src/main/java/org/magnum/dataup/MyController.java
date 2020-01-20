/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.magnum.dataup;

import org.apache.commons.compress.utils.IOUtils;
import org.magnum.dataup.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;


@RestController
public class MyController {
	MyService service_t;

	@Autowired
	public MyController()
	{
		try{
		service_t = new MyService();
		}
		catch(IOException e)
		{
			System.out.println("IO exception at Mycontroller constructor coming from service construction");
		}
	}

//	@GetMapping(value= "/video")
	@RequestMapping(value= "/video", method= RequestMethod.GET)
	@ResponseBody
	public LinkedList<Video> getVidsList(Video vid)
	{
		return service_t.getVidsLis();
	}

	@RequestMapping(value= "/video", method= RequestMethod.POST)
	@ResponseBody
	public Video uploadVideo(Video vid, @RequestParam("file") MultipartFile file)
	{
		Video retVid = Video.create().build(file.getOriginalFilename());
		retVid.setContentType(file.getContentType());
		retVid.setDuration(file.get);

		//Creation de vid
		// Passage a service



		return retVid;
	}



//	@GetMapping("GET /video/{id}/data")
	@RequestMapping(value= "/video/{id}/data", method= RequestMethod.GET)
	@ResponseBody
	public OutputStream getSepecificVid(@PathVariable("id") int id) throws IOException {
		InputStream inStrm = null;
		OutputStream outStrm = null;
		service_t.getStreamById(id, inStrm);
		IOUtils.copy(inStrm,outStrm);
		return outStrm;
	}
}
