import html2Canvas from 'html2canvas'
import JsPDF from 'jspdf'

const htmlToPdf = {
  getPdf (title) {
    console.log('###', document.querySelector('#pdfDom'))
    html2Canvas(document.querySelector('#pdfDom'), {
      allowTaint: false,
      taintTest: false,
      logging: false,
      useCORS: true,
      backgroundColor: '#ffffff',
      dpi: window.devicePixelRatio * 4, // 将分辨率提高到特定的DPI 提高四倍
      scale: 4 // 按比例增加分辨率
    }).then(canvas => {
      // 内容的宽度
      const contentWidth = canvas.width
      // 内容高度
      const contentHeight = canvas.height
      // 一页pdf显示html页面生成的canvas高度,a4纸的尺寸[595.28,841.89];
      const pageHeight = contentWidth / 592.28 * 841.89
      // 未生成pdf的html页面高度
      let leftHeight = contentHeight
      // 页面偏移
      let position = 0
      // a4纸的尺寸[595.28,841.89]，html页面生成的canvas在pdf中图片的宽高
      const imgWidth = 595.28
      const imgHeight = 593.28 / contentWidth * contentHeight
      // canvas转图片数据
      const pageData = canvas.toDataURL('image/jpeg', 1.0)
      // 新建JsPDF对象
      const PDF = new JsPDF('', 'pt', 'a4')
      PDF.addImage(pageData, 'JPEG', 20, 0, imgWidth, imgHeight)
      PDF.addImage(pageData, 'JPEG', 20, position, imgWidth, imgHeight)
      // 判断是否分页
      if (leftHeight < pageHeight) {
        PDF.addImage(pageData, 'JPEG', 0, 0, imgWidth, imgHeight)
      } else {
        while (leftHeight > 0) {
          PDF.addImage(pageData, 'JPEG', 0, position, imgWidth, imgHeight)
          leftHeight -= pageHeight
          position -= 841.89
          if (leftHeight > 0) {
            PDF.addPage()
          }
        }
      }
      // 保存文件
      PDF.save(title + '.pdf')
    })
  }
}

export default htmlToPdf
