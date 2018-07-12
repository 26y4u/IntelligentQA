package com.example.Utils;
import com.example.file.FileBean;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class JsonResult implements Serializable {

     private static final long serialVersionUID = 4997293587553904193L;
     /** * 响应状态 */
     private Integer status;
    /** * 响应信息 */
     private String msg;
    /** * 响应数据 */
    private  Object data;

    public JsonResult(Integer status, String msg, Object data) { this.status = status; this.msg = msg; this.data = data; }
    public static JsonResult build(Integer status,String msg,Object data){ return new JsonResult(status,msg,data); }
    public static JsonResult ok(Object data){ return new JsonResult(200,"ok",data); }
    public static JsonResult refuse(){ return new JsonResult(400,"refuse",null); }
    public static JsonResult refuseforlimit(String msg){ return new JsonResult(400,msg,null); }
    public static JsonResult ok(){ return JsonResult.ok(null); }
    public static JsonResult errMsg(String msg){ return new JsonResult(500,msg,null); }
    public static JsonResult errFile(){ return new JsonResult(501,"error",null); }
    public static JsonResult errMap(Object data){ return new JsonResult(501,"error",data); }
    public static JsonResult errTokenMsg(String msg){ return new JsonResult(502,msg,null); }
    public static JsonResult errException(String msg){ return new JsonResult(555,msg,null); }
    public static JsonResult returnnull(String msg){ return new JsonResult(205,msg,null); }
    public  static  JsonResult refuseservice(String msg){return new JsonResult(403,msg,null);}
    public JsonResult() { }

    @Override
    public String toString() { return "JsonResult{" + "status=" + status + ", msg='" + msg + '\'' + ", data=" + data + '}'; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    /*
    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<FileBean> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(FileBean fileBean) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends FileBean> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends FileBean> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public FileBean get(int index) {
        return null;
    }

    @Override
    public FileBean set(int index, FileBean element) {
        return null;
    }

    @Override
    public void add(int index, FileBean element) {

    }

    @Override
    public FileBean remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<FileBean> listIterator() {
        return null;
    }

    @Override
    public ListIterator<FileBean> listIterator(int index) {
        return null;
    }

    @Override
    public List<FileBean> subList(int fromIndex, int toIndex) {
        return null;
    }
*/
//    @Override
//    public Object[] toArray() {
//        return new Object[0];
//    }
}







