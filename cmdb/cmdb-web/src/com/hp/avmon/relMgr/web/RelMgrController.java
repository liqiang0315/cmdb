package com.hp.avmon.relMgr.web;

import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hp.avmon.relMgr.service.RelMgrService;
import com.hp.avmon.ciMgr.service.CiMgrService;
import com.hp.avmon.common.jackjson.JackJson;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping("/pages/relMgr/*")
public class RelMgrController {

	private static final Logger logger = LoggerFactory
			.getLogger(RelMgrController.class);

	@Autowired
	private RelMgrService classMgrService;
	
	@Autowired
	private CiMgrService ciMgrService;
	
	
	
	@RequestMapping(value = "index")
	public String index(Locale locale, Model model) {

		return "/pages/relMgr/index";

	}
	
	@RequestMapping(value = "getBaseClasses")
	public void getBaseClasses(HttpServletRequest request,
			PrintWriter writer) {
		
		String id = request.getParameter("id");
		String result = classMgrService.getBaseClasses(id);
        writer.write(result);
        writer.flush();
        writer.close();
	}
	
	@RequestMapping(value = "getCiList")
	public void getCiList(HttpServletRequest request,
			PrintWriter writer) {
		
		String id = request.getParameter("id");
		String result = classMgrService.getCiList(id);
        writer.write(result);
        writer.flush();
        writer.close();
	}
	
	
	
    @RequestMapping(value = "getAllClass")
    public void getAllClass(HttpServletRequest request, PrintWriter writer) throws Exception{
           
           String result = null;
           
           result = classMgrService.getAllClass(request);

           logger.debug(result);
           writer.write(result);
     writer.flush();
     writer.close();
    }

	
	@RequestMapping(value = "addClass")
	public void addClass(HttpServletRequest request,PrintWriter writer) {
		
		String id = request.getParameter("id");
		String pid = request.getParameter("pid");
		String name = request.getParameter("name");
		String label = request.getParameter("label");
		String displayIcon = request.getParameter("iconSkin");
		String derivedFrom = request.getParameter("derivedFrom");
		String isEdit = request.getParameter("isEdit");
		String domain = request.getParameter("domain");
		Map result = classMgrService.addClass(id,name,pid,displayIcon,label,derivedFrom,isEdit,domain);
		String json = JackJson.fromObjectToJson(result);
        writer.write(json);
        writer.flush();
        writer.close();
	}
	
	/**
	 * 删除节点
	 * @param request
	 * @param writer
	 */
	@RequestMapping(value = "delClass")
	public void delClass(HttpServletRequest request,PrintWriter writer) {
		
		String typeName = request.getParameter("typeName");
		String ciid = request.getParameter("id");
		String json = classMgrService.delClass(typeName,ciid);
        writer.write(json);
        writer.flush();
        writer.close();
	}
	
	/**
	 * 删除节点
	 * @param request
	 * @param writer
	 */
	@RequestMapping(value = "editClass")
	public void editClass(HttpServletRequest request,PrintWriter writer) {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String json = classMgrService.editClass(id,name);
        writer.write(json);
        writer.flush();
        writer.close();
	}
	
	/**
	 * 删除节点
	 * @param request
	 * @param writer
	 */
	@RequestMapping(value = "getCiTypeAttr")
	public void getCiTypeAttr(HttpServletRequest request,PrintWriter writer) {
		String typeName = request.getParameter("typeName");
		String json = classMgrService.getCiTypeAttrbyName(typeName);
        writer.write(json);
        writer.flush();
        writer.close();
	}
	
	/**
	 * 删除节点
	 * @param request
	 * @param writer
	 */
	@RequestMapping(value = "addCiTypeAttr")
	public void addCiTypeAttr(HttpServletRequest request,PrintWriter writer) {
		String typeName = request.getParameter("typeName");
		String name = request.getParameter("name");
		String label = request.getParameter("label");
		String dataType = request.getParameter("dataType");
		String attGroup = request.getParameter("attGroup");
		String order = request.getParameter("order");
		String defValue = request.getParameter("defValue");
		String viewMode = request.getParameter("viewMode");
		String isRequired = request.getParameter("isRequired");
		String recordChange = request.getParameter("recordChange");
		String updateMode = request.getParameter("updateMode");
		
		String json = (String) classMgrService.addCiTypeAttr(typeName,name,label,dataType,attGroup,order,defValue,viewMode,isRequired,recordChange,updateMode);
        writer.write(json);
        writer.flush();
        writer.close();
	}
	
	/**
	 * 删除节点
	 * @param request
	 * @param writer
	 */
	@RequestMapping(value = "delCiTypeAttr")
	public void delCiTypeAttr(HttpServletRequest request,PrintWriter writer) {
		String attrName = request.getParameter("attrName");
		String typeName = request.getParameter("typeName");

		
		String json = (String) classMgrService.delCiTypeAttr(attrName,typeName);
        writer.write(json);
        writer.flush();
        writer.close();
	}
	
	/**
	 * 删除节点
	 * @param request
	 * @param writer
	 */
	@RequestMapping(value = "getCibyType")
	public void getCibyType(HttpServletRequest request,PrintWriter writer) {
		String typeName = request.getParameter("typeName");
		String json = classMgrService.getCibyType(typeName);
        writer.write(json);
        writer.flush();
        writer.close();
	}
	
	/**
	 * 删除节点
	 * @param request
	 * @param writer
	 */
	@RequestMapping(value = "addCibyType")
	public void addCibyType(HttpServletRequest request,PrintWriter writer) {
		String typeName = request.getParameter("typeName");
		String name = request.getParameter("name");
		String label = request.getParameter("label");
		String json = classMgrService.addCibyType(typeName,name,label);
        writer.write(json);
        writer.flush();
        writer.close();
	}
	
	/**
	 * 删除节点
	 * @param request
	 * @param writer
	 */
	@RequestMapping(value = "updateCibyType")
	public void updateCibyType(HttpServletRequest request,PrintWriter writer) {
		String typeName = request.getParameter("typeName");
		String name = request.getParameter("name");
		String label = request.getParameter("label");
		String id = request.getParameter("id");
		String json = classMgrService.updateCibyType(typeName,id,name,label);
        writer.write(json);
        writer.flush();
        writer.close();
	}
	
	/**
	 * 删除节点
	 * @param request
	 * @param writer
	 */
	@RequestMapping(value = "addRelationCi")
	public void addRelationCi(HttpServletRequest request,PrintWriter writer) {
		String typeName = request.getParameter("typeName");
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String label = request.getParameter("label");
		String sourceNodeId = request.getParameter("sourceNodeId");
		String sourceNode = request.getParameter("sourceNode");
		String sourceType = request.getParameter("sourceType");
		String targetNodeId = request.getParameter("targetNodeId");
		String targetNode = request.getParameter("targetNode");
		String targetType = request.getParameter("targetType");
		
		String json = (String) classMgrService.addRelationCi(typeName,name,label,sourceNodeId,sourceNode,sourceType,targetNodeId,targetNode,targetType);
        writer.write(json);
        writer.flush();
        writer.close();
	}
	
	@RequestMapping(value = "getRelationList")
	public void getRelationList(HttpServletRequest request,
			PrintWriter writer) {
		String result = ciMgrService.getRelationList();
        writer.write(result);
        writer.flush();
        writer.close();
	}
	
	
}
