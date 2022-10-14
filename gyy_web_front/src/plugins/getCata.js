import Store from '../store/index'

const initStore = () => {
  Store.commit('setCurrentCatalogArr', [])
  Store.commit('setCurrentCatalogIndex', '')
  Store.commit('setNextCata', {})
  Store.commit('setCurrentCata', {})
}
/**
 * 1:有子目录的卷 currentCatalogArr
 * 2:无子目录的卷 nextCata
 * 3:非最后一个章 currentCatalogArr
 * 4:最后一个章 nextCata
 */
const getNextCata = (item, arr) => {
  Store.commit('setCurrentCatalogArr', [])
  Store.commit('setCurrentCatalogIndex', '')
  Store.commit('setNextCata', {})
  Store.commit('setCurrentCata', {})
  // 有子目录，那么下一章目录就往子数组中跳
  if (item.cataLog && item.cataLog.length !== 0) {
    Store.commit('setCurrentCatalogArr', item.cataLog)
    Store.commit('setCurrentCatalogIndex', -1)
    window.sessionStorage.setItem('store', JSON.stringify(Store.state))
    return 1
  } else {
    if (item.grade === 1) {
      for (let i = 0; i < arr.length; i++) {
        if (parseInt(arr[i].id) === parseInt(item.id) + 1) {
          // Store.commit('setCurrentCatalogArr', element)
          // console.log(element)
          Store.commit('setCurrentCata', arr[i - 1])
          Store.commit('setNextCata', arr[i])
          // console.log('wuzi', Store.state.currentCatalogArr) // length=0
          window.sessionStorage.setItem('store', JSON.stringify(Store.state))
          return 2
        }
      }
    } else {
      for (let i = 0; i <= arr.length; i++) {
        if (Math.floor(parseInt(item.id) / 1000) === parseInt(arr[i].id)) {
        //   console.log(arr[i]) // 父对象
        //   console.log(parseInt(item.id) % 1000) // 取后三位
          // 如果是最后一章，就要查找下一卷
          if (parseInt(item.id) % 1000 === arr[i].cataLog.length) {
            for (let j = 0; j < arr.length; j++) {
              if (Math.floor(parseInt(item.id) / 1000) === parseInt(arr[j].id) - 1) {
                Store.commit('setCurrentCata', arr[j - 1]) // 且为数组最后一个元素
                Store.commit('setNextCata', arr[j])
                window.sessionStorage.setItem('store', JSON.stringify(Store.state))
                // console.log('4', Store.state.nextCata)
              }
            }
          }
          Store.commit('setCurrentCatalogArr', arr[i].cataLog)
          Store.commit('setCurrentCatalogIndex', parseInt(item.id) % 1000 - 1)
          window.sessionStorage.setItem('store', JSON.stringify(Store.state))
          return 3
        }
      }
    }
  }
}

export default { getNextCata, initStore }
