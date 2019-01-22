package org.alvin.code.v2.core.action;


import org.alvin.code.v2.core.dao.CodeDao;
import org.alvin.code.v2.core.model.CodeCond;
import org.alvin.code.v2.core.model.Field;
import org.alvin.code.v2.core.model.Table;
import org.alvin.code.v2.core.service.CodeService;
import org.alvin.code.v2.core.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @功能描述:代码生成器控制器类
 * @author gzz_gzz@163.com
 * @date 2018-02-15
 */
@RestController
@RequestMapping("/api/code")
public class CodeAction {
	@Autowired
	private CodeService service;// 生成器业务罗辑接口
	/**
	 * @功能描述: 查询数据库中表名列表
	 */
	@PostMapping("/queryList")
	public List<Table> queryList(@RequestBody CodeCond cond) {
		cond.setDb_user(CodeDao.DBUSER);
		return service.queryTables(cond);
	}

	/**
	 * @功能描述: 查询数据库中表名列表
	 */
	@PostMapping("/queryField")
	public List<Field> queryField(@RequestBody CodeCond cond) {
		cond.setDb_user(CodeDao.DBUSER);
		return service.queryFields(cond);
	}

	/**
	 * @功能描述: 生成代码
	 */
	@PostMapping("/create")
	public void create(@RequestBody CodeCond cond) throws Exception {
		Utils.delDir(new File(Utils.path() + "com/dl/"));
		cond.setDb_user(CodeDao.DBUSER);
		Utils.setTime();
		service.create(cond);
		Utils.chmod();
	}

	@GetMapping("/downCode")
	public void downCode(HttpServletResponse response) throws IOException {
		String fileName = "code.zip";
		Utils.createZip(Utils.path() + "com", Utils.path() + fileName);
		Path path = Paths.get(Utils.path() + fileName);
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		response.setHeader("Content-Length", "" + Files.size(path));
		response.setContentType("application/zip");
		OutputStream out = response.getOutputStream();
		out.write(Files.readAllBytes(path));
		out.flush();
		out.close();
	}

	@GetMapping("/executeSql")
	public void executeSql(String sql) {
		service.executeSql(sql);
	}
}
