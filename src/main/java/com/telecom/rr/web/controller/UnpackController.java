package com.telecom.rr.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.telecom.rr.FrameController;
import com.telecom.rr.domain.FD;
import com.telecom.rr.commons.json.Result;

/**
 *
 */
@Controller
@RequestMapping("unpack")
public class UnpackController extends FrameController {

    @RequestMapping("")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String authorization = request.getHeader("authorization");

        if (authorization == null || !"Basic YWRtaW46a29icmE7".equals(authorization)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("WWW-Authenticate", "Basic realm=\"Authenticate\"");
            return null;
        }

        ModelAndView mv = new ModelAndView("unpack/index");

        String dir = request.getSession().getServletContext().getRealPath("release");

        File unpack = new File(dir + "/rr/unpack.xml");
        if (unpack.exists()) {
            mv.addObject("unpackxml", StringEscapeUtils.escapeHtml(FileUtils.readFileToString(unpack,"UTF-8")));
        }

        File enabled = new File(dir + "/rr/enabled");
        if (enabled.exists()) {
            mv.addObject("enabled", StringEscapeUtils.escapeHtml(FileUtils.readFileToString(enabled,"UTF-8")));
        }

        File filelist = new File(dir + "/rr/file.list");
        if (filelist.exists()) {
            mv.addObject("filelist", StringEscapeUtils.escapeHtml(FileUtils.readFileToString(filelist,"UTF-8")));
        }

        mv.addObject("dir", dir);
        return mv;

    }

    @RequestMapping("/list")
    @ResponseBody
    public Result list(HttpServletRequest request, @RequestParam(required = false) String dir) {
        String authorization = request.getHeader("authorization");
        if (authorization == null) {
            return new Result(Result.ERROR, "未验证的请求", null);
        }
        String path = request.getSession().getServletContext().getRealPath("release");
        if (dir == null || dir.equals("")) {
            dir = path;
        } else {
            dir = path + "/" + dir;
        }

        File[] files = list(dir);
        List<FD> fds = Lists.newArrayList();

        for (File e : files) {
            String _dir = e.getPath().replace(path, "");
            FD fd = new FD();
            fd.setDir(_dir);
            fd.setDirectory(e.isDirectory());
            fd.setName(e.getName());
            fd.setLastModify(e.lastModified());
            if (e.isFile()) {
                try {
                    fd.setMd5(DigestUtils.md5DigestAsHex(FileUtils.readFileToByteArray(e)));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    // ignore
                }
            }

            fds.add(fd);
        }

        return new Result("", fds);
    }

    @RequestMapping("/upload")
    public ModelAndView upload(HttpServletRequest request, @RequestParam MultipartFile zip, @RequestParam String version)
            throws Exception {

        String authorization = request.getHeader("authorization");

        ModelAndView mv = new ModelAndView("unpack/callbackupload");
        if (authorization == null) {
            mv.addObject("error", "未验证的请求");
            return mv;
        }

        String uploadFilename = "rr.zip";

        try {
            version = version.trim();
            String suffix = FilenameUtils.getExtension(zip.getOriginalFilename());
            if (!suffix.endsWith("zip")) {
                mv.addObject("error", "文件必须为zip格式");
                return mv;
            }
            String dir = request.getSession().getServletContext().getRealPath("release");
            File outdir = new File(dir + "/rr/" + version);
            if (!outdir.exists()) {
                outdir.mkdirs();
            }
            File outfile = new File(outdir, uploadFilename);
            if (outfile.exists()) {
                outfile.delete();
            }
            outfile.createNewFile();
            FileCopyUtils.copy(zip.getInputStream(), new FileOutputStream(outfile));

            String md5 = "";
            try {
                md5 = DigestUtils.md5DigestAsHex(FileUtils.readFileToByteArray(outfile));
            } finally {

            }

            mv.addObject("dir", dir);

            ZipFile file = new ZipFile(outfile);
            StringBuilder filelistsb = new StringBuilder();
            try {
                Enumeration<? extends ZipEntry> entry = file.entries();

                while (entry.hasMoreElements()) {
                    ZipEntry e = entry.nextElement();
                    filelistsb.append(e.getName());
                    if (!e.isDirectory()) {
                        filelistsb.append("  <" + e.getSize() + ">");

                    }
                    filelistsb.append("\n");
                }

            } finally {
                if (file != null) {
                    try {
                        file.close();
                    } catch (Exception e) {}
                }
            }

            FileUtils.writeStringToFile(new File(dir + "/rr/unpack.xml"), //
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + //
                            "<release>\n" + //
                            "  <version>" + version + "</version>\n" + //
                            "  <name>" + uploadFilename + "</name>\n" + //
                            "  <md5>" + md5 + "</md5>\n" + //
                            "  <size>" + outfile.length() + "</size>\n" + //
                            "</release>", "UTF8");

            FileUtils.writeStringToFile(new File(dir + "/rr/file.list"), filelistsb.toString(), "UTF8");
            FileUtils.writeStringToFile(new File(dir + "/rr/enabled"), "0", "UTF8");

        } catch (Exception e) {
            mv.addObject("error", "上传失败: " + e.getMessage());

        }
        return mv;

    }

    @RequestMapping("/enabled")
    public ModelAndView enabled(HttpServletRequest request, @RequestParam String enabled) throws Exception {

        String authorization = request.getHeader("authorization");

        ModelAndView mv = new ModelAndView("unpack/callbackenabled");
        if (authorization == null) {
            mv.addObject("error", "未验证的请求");
            return mv;
        }
        String dir = request.getSession().getServletContext().getRealPath("release");
        try {
            FileUtils.writeStringToFile(new File(dir + "/rr/enabled"), enabled + "");

        } catch (Exception e) {
            mv.addObject("error", "请求失败: " + e.getMessage());

        }
        return mv;

    }

    @RequestMapping("/remove")
    public ModelAndView remove(HttpServletRequest request, @RequestParam String dir) throws Exception {

        String authorization = request.getHeader("authorization");

        ModelAndView mv = new ModelAndView("unpack/callbackremove");
        if (authorization == null) {
            mv.addObject("error", "未验证的请求");
            return mv;
        }

        try {
            String path = request.getSession().getServletContext().getRealPath("release");
            File outdir = new File(path + dir);
            if (outdir.getName().endsWith("unpack.xml") || outdir.getName().endsWith("unpack-demo.xml")
                    || outdir.getName().endsWith("enabled") || outdir.getName().endsWith("file.list")) {
                mv.addObject("error", "unpack.xml & unpack-demo.xml & enabled & file.list 不能被删除");
                return mv;
            } else if (!outdir.exists()) {
                mv.addObject("error", "文件不存在");
                return mv;
            } else {
                delete(outdir);
                mv.addObject("dir", dir);
            }
        } catch (Exception e) {
            mv.addObject("error", "上传失败: " + e.getMessage());

        }
        return mv;

    }

    public File[] list(String dir) {
        File file = new File(dir);
        File[] files = file.listFiles();
        return files;
    }

    public void delete(File outdir) {

        if (outdir.exists()) {
            if (outdir.isFile()) {
                outdir.delete();
            } else if (outdir.isDirectory()) {
                File files[] = outdir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    delete(files[i]);
                }
                outdir.delete();
            }
        }
    }

}
