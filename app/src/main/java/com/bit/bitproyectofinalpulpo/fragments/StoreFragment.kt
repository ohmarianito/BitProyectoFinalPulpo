package com.bit.bitproyectofinalpulpo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bit.bitproyectofinalpulpo.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_store.*
import kotlinx.coroutines.tasks.await


class StoreFragment : Fragment() {

    private lateinit var adapter:StoreAdapter

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_store, container, false)

        /*rvStore.layoutManager = LinearLayoutManager(context) //este seria el this
        rvStore.adapter = StoreAdapter(context) //hay que arreglar esto*/

        adapter = StoreAdapter(context!!)

        var recyclerView = view.findViewById<RecyclerView>(R.id.rvStore)

        recyclerView.layoutManager = LinearLayoutManager(context!!) //este seria el this
        recyclerView.adapter = adapter



/*
        val dummyList = mutableListOf<Producto>()
        dummyList.add(Producto("1", "Producto1", "100", "Descripcion del producto1", "https://www.vamosajugar.com.uy/wp-content/uploads/2019/11/moto-12v-02.jpg"))
        dummyList.add(
            Producto("2", "Producto2", "20", "Desc del producto2", "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAoHCBISEhISERISEhISERESEhISEhEREhIRGBUZGRgUGBgcIS4lHB4sHxgaJjg0Ky8xNTU1GiQ7QDs0Py40NTEBDAwMEA8QHxISHzQsJSs2NDE0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NP/AABEIALcBEwMBIgACEQEDEQH/xAAbAAEAAgMBAQAAAAAAAAAAAAAABAUBAgMGB//EADsQAAIBAwIEBAMHAgMJAAAAAAABAgMEERIhBTFBYQYiUXETgZEUMkJSocHRI7EVcuEHU2JzkqKy8PH/xAAaAQEAAgMBAAAAAAAAAAAAAAAABAUBAgMG/8QALREAAgIBBAECBAUFAAAAAAAAAAECAxEEEiExBRNBIlFhcRQyQoGRI6Gx0eH/2gAMAwEAAhEDEQA/APswAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMZMgAAAAGMmQAAAAAAAAAAAAAAAAAAAAAAAAAAAADGTIAAMZAMgxkZAMgwMgGQYyc51ox+80gYbS5Z1MEL/E6Wca1+31I3FOLqi6aUVJTUmpZwlpx27mls1VHdPoUyjdLbW039Gi1yZyeclxmo+WhLphNv65OMuI1nzm/kor+yIEvK0LrLJa0lj7weoNJ1oR5yivdpHlJ1pvnOT95M5s4S8uv0x/udFon7yPVxvKbelTi37ndM8Y2vVEq24rKntq1x/LLP6PobU+TTfxxx9UYnopfpeT1Rki2N0qsFUimk29nz2eCSWsZKSyiG1h4ZkAGxgAAAAAAAAAAAAAAAAAAAGMgGDWU0upXcSuZQnHG8ZRe3dNfycqPEYS5+V9919St1GtdcnCK5+p2jTJx3exZut6L6nOU2+pzi0900123RnJXWaq2zt/wFFI3U2joq3qitr8Rp09nJN+kd2Vdxxqb+4lFer3ZtVffDp/ybKrcehuL2FNZlq+UZP9eSK2rxx/gh85P+Dzte9x56s9k/vTlhfI7RqJrOVjnnO2Pfkeg0M1dByn2ec81qtRpbFCt8NZLCrxKrL8WF6RwQ5Sb3bb9yHV4jShzmm/SPm/XkQqvG/yQ+cn+uxY/DE89KWovfxNv+S4wcbqdPGKkopLOMvdeyKq3+1XUnCmpNpKUkmqajF7KTzvjPpkj8XsKlrVtqdRxcrjW24tvQouKe/VvUjjddXse/lEzSeO1TmnXlP5rgky4lClJaJSnBzjFqUdlnk0+fP+5Olfv0SXr/8ATjwu8tLf7X8adOM9EadKFTzSkpQU2kuqckv+kp5VlT4bBTk8uEYtybbcm29315HndXpqpTUq1hP2PdaC6yFLjqHukvc9BZzqV9Xwf6mlpS0NNRb3SbztsRqV3GpTqVYylKFKU4yeJRblBJyUU+fNb8uZ57wx4yVlTqU4Utc6k1LVJ6YpJYSxzJbr/C4bN9ZuTefWdRR/99jFmjjCMe8s7Q8hucsYwj1XBLH7VSVbU4QcpLS1mXlbT3ztyInhjF3FTmkoyqTjDTlZguUn3aPM+H/Gda2h8FxjUptNRivLKEpJ7p+769z1/gekoU6UekacpcuucHazSwg4RS5fZxp107Yzkn0ewtbeNOKhBYis43zzef7s7nL4qHxSySxwiLnJ2Bx+IbKZkHQGmozkA2BrkzkAyDAAMgAAAAAAGGADznH+LSo1FDU4LSpJpZz6+x6I8z4v4XKrBVYNZpxlqi+bi2uXc2hKMXlkTXV2WUuNTeeOuyk/x6FScIapz1TUcy5Rb99+iN53bi8Ti4tPmt4lPwPhbq3MISk4JS15Sy3p82lZ5Zxz/QteK3lKnOcZyWVKSwvM+foir8k4TanD7E7wVF8ISrszntZ5J9pdvnCW3Z7fwcbu+qTk1Kbwm/Ktlj9ykpSjJ66U3F+sXh+zXU7NzbbnLOcckolbGKyXFlG3tFlZWs62fhpNReJPKUYvCeH6PDK7xK6lpWt6WYNVdMpyw9vPhxj8iv4D4jlw+pXpypxqUp1pSajJxqQa8u2dpbLt7nXxfxu3vJWlShJtw1qcZRcZweqDSa+vVrYnKur0srsi/wBT1En0cKnBPtt2qLqOEvs06kJNalrjKKw16PV0Iz4dWtqitKzXxHqnTxJyg6fJSXpyZKqcWnZ3UK9Onrl8GpDDzpWqUXl4/wApDr8Ynd3dOrUSUoxcEkkko5bx+p3p1LhXhdkPUeOrvnun0SLB0ldqjcz0UowcpVIy0ty0qSS274O/Hri0nUtIWkXFQrJzlmXnWqGM5eXyZ5nxA07ieU86af4nj7q6HLhD/qw/zw/8kYnqLZww2b1aDT1cxieoqeIatldOdOMJKdKCnGbfmSlJ7Ncn9RxTjsL66takISpunTcJxlh4lqb2a5opeOUJVK+V/u4LtzkY4fb/AApa85x0/wBSP6jVezJN9FN7sEbjVxF3NRbNpxT9eSLXjeXaUoJbuUHt2i/5PO8Yv6ELinPMXNup8XRiUs4WhS35ZNo8bc86Kcml+KpJQXyS1NlhGtShC2XCXBTTrvVllNUdzlzn5G1Phk8p7LDTw+x6C9qRqW8KKbWHBy9NnnHfc8tW4vWTxilHPJxUm/8Au/gg3XEquHmpJdk1FfRFh+DjbFWt8LlEBV6upyplhN8NHp/slOGNTx6apKO/zLmwvpTlSlCaUKVReaDUcRTWpZT32z2PI8MtISjNyqRjOnHX/Ucpa/Xd7pe3qiRwxy0qME+beF6siK2Fkn8PK6ZPs8PPTwTlb32kfWY8bg/xepKpcTT6nz2ytarxnKPQWVvNYyzc6Hrad3kkwrZKS2gyyopgFhGZ0jIjwR3jEA3TNjEYm2ADAGAAbgAAAGGAZNSg8QXlWnKEYqThOMs6FlprHN+mH+h5m58UOjrjSc3LDTbS0xfrudIwzFtP9iut8jGu/wBFxf39v+nv7m4hTi51JxhFLLcpKKS92eV4t4yt9MoUlKtq8rkliCT5tN/e+Sx3PBXN9UrvXVnOpLfDlLKT7Lkvkjk2ipt1UnmMUe203ha8Kdks+/HCLPiV/HTN0pvW15dOpSz+yKNyljLby+ed8skxTlyXzN1bxW82Q4x4wW1VUKPykelcSg1OPTmujXoz0dK4U4RlF5UkmvU81dcRpwTjFJ9CV4dutUZR/LKWF25/uZdWFkheQalEgcUg/tFRLrLP1Sl+5rSsZZUm9Pbnkl8Wv6VGcpTklKWGklmbWlY2/T5FPW4vOa8q+HF/Ob+fQ6KNjXCx9yBVFT4Ra8X4m4OOrdSzlLZr2Rrw6pGVSFSLTinu/Tbk10Z5e5oasyTev82W233zzNOF8a+FGacNUnhp5xHb1JMdJvhmvlnC5+lLbZwvoevvqUalSU87Yiu7wsEercUaOHKUYyW6W8p+6S3KmnVuK6b32WXCn5ML3zl/uUtaahJxSbfolvk20ulhbLFksY+RrqpTor3wWfv2X134h3xSg228KVR/rpRzuo1p0/iTcnDOMxkoxT5fdTyUdOhWnKMowwk+rL+34dWlD4bnppuWtwXmy/fBItorhNeguPr2cqNUvSk7e/l/o8xVo1JS1KOp56F5wS5nTcGqU3pTUoaHvldHhnpbLhKilhFvbcNXodJx3rEiLTe6ZNxXfB4eXCa9xNycfgwcm1FbPd5wl0XpksaHg2m8a3OT9XJnu7fh/YsaFgvQ2jwtqOU5ucnJ9s8bZ+EqW2pTljlmT2PR2HBYU0lCCS9ty/o2i9CbSt16GEkuhKcpfmZWUOH9ifSsuxYU6BJhSMmpCpWmCVToEmMDpgA5RpnVIyAAAAAAAADDZq5AG5q2aNmGwCk8QVpUYyq5ejSoyWVz5fqfHuK3rc1LDUZVMyXXG6PrvijidrTpVKFxNt1INfDgtVTDziWOS3XXC2Pjd7TcttTai9s82u/0OLpdSlZh4l7+x2poWqthslHdBptPvBaUqba8uyfI6uEYbyZUSvpwgku+GVF7xLrOXyTK+NLkz2NmojCPLwehueLxjtBZKK+4q39+WO2SlqX857QWF6vmKNjKbzLPzLPT+NnZ2Ueq8zCP5ORW4jKW0F8z03hG5xJRlmLaaWerx6lTRtYQ6ZJFte6JxlTjrcG9s6Y9Vhy/gsL/ABlMKXveH7FLDXX6i1KKyTPFdHN3Tk1t9nj82p1P2kjhZ0fiTUNSi3yz7ZId1xKrVr5raV5cQUViKWc4T5t+5o5YaaeMcn1T9yilBpKL+R6PS4Vbxw8vJIuYOE5Rym4vGVyexV1+GzTbgsp52LChRnN8ml1b6l/ZW2NsEqiLiis196nNRXsUlh9oi24JwclplnGGvb5E2y4Ph6peaT5s9JQs0+hPoWCN4wSeURLb52Y3FNbcP7FtbWHYs6NmWFG1NziQbez7FlQtSVStybTpAEelbL0JdO3O8KZIhAA5QpEiEDeEDrGIBiEDqkIo2AAAAAAAAAAAAAObMMyzVgDJFv7yFGnOpUemME23lLLxslnq+RJPL+P5Ysp/8ylj3yzpTD1LIw+bwc7ZbYNnhry5lXnUqT+9ObljpFfhiuyWF8iqurZ84fQm2s1KO2M77dTvoPVTqg4+lJcdYKGjU202epB4Z4njCryaUIaYrOZJ5b/QrqXDJvef1Z7PjE401FJLXLL9kup52vdrVpzrn6L930IP4PSaeLn19y1/G6nVy55bNKVrGPTJmVws6YeaXVLlH3ZpWpzknmWnslt831NeHONPGpavP58c2iv1HlYKGNOv3LLT+Isc09R13x/g5XyqOLepY6xSawv3LLhKoJ6Kz0x07POlavc045UpTko20d5R82lNRy8ep24ZwOcktbbXpyKmc7dTFObJ6np9LJxgsZXsVVzbOtUxTb0Qb0y6tZ2/YurDgzWHLMmvzfwejsODxgkoxwW9Hh/Y6RikkiBZdKc3LPZRW/DuxZW9j2LmlY9iZTtOxscisoWhPo2/Ym07XsSoW4BFpUCZToneFE7wpgHKFMkQgbwgdYwAMQgdoxEYnSMQBGJ0SCRsAAAAAAAAAAAAAAAAc2as2wMAHNnmPHsHKynj8M6U37KWP3PUtFRx+1dWhVppZc6clFf8WMx/VI60T2Wxl8mmaWR3QcfofGMnenfTjzepej/kiXE1CbpzajOLacW0nnOPmcqk9n7M9k51zXDTKHY8/EiBfXkqk3N7Z5JPklyWSBaYjOo+urb2aMVayW27fSK3bJFlwerVnreqmmksLnjueb8nKM4bM8l546foWKbXBYXt/QnQhGMMVtk/LiSae+ZdckTh3B6tSTlKUkpPLiv9T0/DPDaWG45fq939T01nwjHQp66lFFhfqZTeI5SR57h3A4Rw9Kz9f7noLbh+Ohb0OH46E+ladjqRct8srKNl2JtO07FjC3O8KIMEGFt2O8KHYmRpHWNMAiRonWFIkxgbxgAcI0zpGB2UDKiAaRgbqBuomyQBrGJukZAAAAAAAAAAAAAAAAAAABqYNjGADVo5yhk6jABR3/h22r5+JSpyb6uKyebvf9mtpLLp66WfySaX0PftGrRlSa6ZjCPmtp/s2o0nlNzfrLdlxQ8OQhyR69wNdBgyUFPhaXQkws0uhbaDGgAgwtkdo0SUoGdABwjTNlA7KJlRAOagbqBsomyQBoomyibJGUgDCiZwZMgGDIAAAAAAAAAAAAAAAAAAAAAABgAABmDJgAwYaNsGMAGuBg2wMAGmkaTcYANMGcG2DOADXAwbYGADCRnBkAAGQAAAAAAAAAAAAAAAAAAAAAAAAAAAADAAAAABgAAAIAAAAAGQAAAAAZAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP/2Q=="))
        dummyList.add(Producto("3", "Producto3", "30", "Desc", "https://www.vamosajugar.com.uy/wp-content/uploads/2019/11/moto-12v-02.jpg"))

        adapter.setListData(dummyList)
        adapter.notifyDataSetChanged()



*/


        getProductsFromFirebase(object: ProductsListCallback {
            override fun onCallback(productos:MutableList<Producto>) {
                adapter.setListData(productos)
                adapter.notifyDataSetChanged()
            }
        })

        //start(view)
       /* val list = getListOfProducts()
        adapter.setListData(list)
        adapter.notifyDataSetChanged()*/

        return view
    }

    /*
    private fun start( view: View){
        view.buttonCloseSession.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

            //Como costo llegar a esta linea jaja
            getActivity()?.onBackPressed();

            //esto redirige a actividad login pero no mata las capaz de abajo si le doy al boton back vuelve a la app

            //val i = Intent(activity, AuthenticationActivity::class.java)
            //startActivity(i)
            //(activity as Activity?)!!.overridePendingTransition(0, 0)

            // esto aca cierra app y mata todito es una bomba nuclear

            // exitProcess(-1)
            println("other message   CERRO SESSION ")
        }
    }

     */


    private fun getDataFromFirebase(view: View): MutableList<Producto> {
        // busca los datos en firebase
        var mutableData = MutableLiveData <MutableList<Producto>>()
        var listData = mutableListOf<Producto>()
        db.collection("productos").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var productoId = document.id
                    /*val productoNombre = document.getString("productoNombre")
                    val productoValor = document.getString("productoValor")
                    val productoDescripcion = document.getString("productoDescripcion")
                    val productoURL = document.getString("productoURL")*/
                    var productoNombre = document.get("productoNombre") as String?
                    var productoValor = document.get("productoValor") as String?
                    var productoDescripcion = document.get("productoDescripcion") as String?
                    var productoURL = document.get("productoURL") as String?
                    var producto = Producto(
                        productoId,
                        productoNombre!!,
                        productoValor!!,
                        productoDescripcion!!,
                        productoURL!!
                    )
                    listData.add((producto))
                }

                mutableData.value = listData

                }
            .addOnFailureListener { exception -> println("no funca pues $exception")  }

        return listData
            }

    private suspend fun getListOfProducts(adapter: StoreAdapter) {
        var listData = mutableListOf<Producto>()
        val snapshot = db.collection("productos").get().await()
        for (document in snapshot) {
            var productoId = document.id
            /*val productoNombre = document.getString("productoNombre")
            val productoValor = document.getString("productoValor")
            val productoDescripcion = document.getString("productoDescripcion")
            val productoURL = document.getString("productoURL")*/
            var productoNombre = document.get("productoNombre") as String?
            var productoValor = document.get("productoValor") as String?
            var productoDescripcion = document.get("productoDescripcion") as String?
            var productoURL = document.get("productoURL") as String?
            var producto = Producto(
                productoId,
                productoNombre!!,
                productoValor!!,
                productoDescripcion!!,
                productoURL!!
            )
            listData.add((producto))
        }
        adapter.setListData(listData)
        adapter.notifyDataSetChanged()
    }

    interface ProductsListCallback {
        fun onCallback(value:MutableList<Producto>)
    }

    fun getProductsFromFirebase(myCallback:ProductsListCallback) {
        var mutableData = MutableLiveData <MutableList<Producto>>()
        var listData = mutableListOf<Producto>()
        db.collection("productos").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var productoId = document.id
                    /*val productoNombre = document.getString("productoNombre")
                    val productoValor = document.getString("productoValor")
                    val productoDescripcion = document.getString("productoDescripcion")
                    val productoURL = document.getString("productoURL")*/
                    var productoNombre = document.get("productoNombre") as String?
                    var productoValor = document.get("productoValor") as String?
                    var productoDescripcion = document.get("productoDescripcion") as String?
                    var productoURL = document.get("productoURL") as String?
                    var producto = Producto(
                        productoId,
                        productoNombre!!,
                        productoValor!!,
                        productoDescripcion!!,
                        productoURL!!
                    )
                    listData.add((producto))
                }

                mutableData.value = listData
                myCallback.onCallback(listData)

            }

    }

    }


