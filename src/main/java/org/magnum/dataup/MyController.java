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
import org.magnum.dataup.model.VideoStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
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
	public LinkedList<Video> getVidsList()
	{
		return service_t.getVidsLis();
	}

	@RequestMapping(value= "/video", method= RequestMethod.POST)
	@ResponseBody
	public Video uploadVideoData(@RequestBody Video vid)
	{
		vid.setId(MyService.last_id.incrementAndGet());
		vid.setDataUrl(getDataUrl(vid.getId()));
		service_t.addVideoInf(vid);

		return vid;
	}

	@RequestMapping(value= "/video/{id}/data", method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<VideoStatus> uploadVideo(@RequestParam("file") MultipartFile file, @PathVariable("id") long id)
	{
		try
		{
			service_t.storeFile(id , file);
		}
		catch(IOException e)
		{
			return  new ResponseEntity( HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(new VideoStatus(VideoStatus.VideoState.READY), HttpStatus.OK);

	}


	@RequestMapping(value= "/video/{id}/data", method= RequestMethod.GET)
	@ResponseBody
	public OutputStream getSepecificVid(@PathVariable("id") int id) throws IOException {
		InputStream inStrm = null;
		OutputStream outStrm = null;
		service_t.getStreamById(id, inStrm);
		IOUtils.copy(inStrm,outStrm);
		return outStrm;
	}

	private String getDataUrl(long videoId){
		String url = getUrlBaseForLocalServer() + "/video/" + videoId + "/data";
		return url;
	}

	private String getUrlBaseForLocalServer() {
		HttpServletRequest request =
				((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String base =
				"http://"+request.getServerName()
						+ ((request.getServerPort() != 80) ? ":"+request.getServerPort() : "");
		return base;
	}

}
