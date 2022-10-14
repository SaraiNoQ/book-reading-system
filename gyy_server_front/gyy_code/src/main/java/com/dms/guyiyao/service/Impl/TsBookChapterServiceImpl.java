package com.dms.guyiyao.service.Impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dms.guyiyao.mapper.TsBookChapterMapper;
import com.dms.guyiyao.pojo.CataLog;
import com.dms.guyiyao.pojo.TreeNode;
import com.dms.guyiyao.pojo.TsBookChapter;
import com.dms.guyiyao.service.ITsBookChapterService;
import com.dms.guyiyao.utils.Loggers;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.xml.bind.util.JAXBSource;
import java.util.List;

/**
 * <p>
 * 医书序卷章节表 服务实现类
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Service
public class TsBookChapterServiceImpl extends ServiceImpl<TsBookChapterMapper, TsBookChapter> implements ITsBookChapterService {
@Autowired
TsBookChapterMapper bookChapterMapper;

    @Override
    public List<TreeNode> getChapter(String bookName) {
//从数据库种获取当前书籍所有的章节信息
         List<TreeNode>nodeList=bookChapterMapper.getChapterNode(bookName);
         Loggers.info("获取到节点数量:"+(nodeList==null?0:nodeList.size()));
//         构建树结构并返回
         TreeBuild treeBuild = new TreeBuild(nodeList);
        List<TreeNode> nodeList1 = treeBuild.buildTree();
        Loggers.info("一级节点数量:"+(nodeList1==null?0:nodeList1.size()));
        return  nodeList1;
    }
}