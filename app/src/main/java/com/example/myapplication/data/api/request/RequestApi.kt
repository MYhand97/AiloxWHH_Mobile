package com.example.myapplication.data.api.request


import com.example.myapplication.data.api.response.penerimaan.ResponseEceranGetTmpReceive
import com.example.myapplication.data.api.response.penerimaan.ResponseEceranPostTmpReceive
import com.example.myapplication.data.api.response.penerimaan.ResponseEceranValItem
import com.example.myapplication.data.api.response.ResponseGetMainMenu
import com.example.myapplication.data.api.response.penerimaan.ResponseGetPenerimaanEceran
import com.example.myapplication.data.api.response.ResponseGetSubMenu
import com.example.myapplication.data.api.response.ResponseSelectDB
import com.example.myapplication.data.api.response.penerimaan.ResponseRcptHeader
import com.example.myapplication.data.api.response.penerimaan.ResponseRcptLPN
import com.example.myapplication.data.api.response.ResponseUserLogin
import com.example.myapplication.data.api.response.pengambilan.all.ResponsePickingCheckScanLoc
import com.example.myapplication.data.api.response.pengambilan.all.ResponsePickingDataModLocItemList
import com.example.myapplication.data.api.response.pengambilan.all.ResponsePickingModInValLPN
import com.example.myapplication.data.api.response.pengambilan.all.ResponsePickingDataModLocList
import com.example.myapplication.data.api.response.pengambilan.all.ResponsePickingDataModWaveList
import com.example.myapplication.data.api.response.pengambilan.all.ResponsePickingGetTmpPick
import com.example.myapplication.data.api.response.pengambilan.all.ResponsePickingPackTypeList
import com.example.myapplication.data.api.response.pengeluaran.ResponseDispModSoList
import com.example.myapplication.data.api.response.pengeluaran.ResponseDispModWaveList
import com.example.myapplication.data.api.response.penyimpanan.ResponseDataCheckLocation
import com.example.myapplication.data.api.response.penyimpanan.ResponseDataPutawayGetAllocation
import com.example.myapplication.data.api.response.penyimpanan.ResponseDataPutawayGetItemInLPN
import com.example.myapplication.data.api.response.penyimpanan.ResponseDataPutawayGetMasterLocation
import com.example.myapplication.data.api.response.penyimpanan.ResponseDataPutawayModDisplayLPN
import com.example.myapplication.data.api.response.penyimpanan.ResponsePutawayGetPalletNumber
import com.example.myapplication.data.api.response.penyimpanan.ResponseSaveItem
import com.example.myapplication.models.ModelsSelectDB
import com.example.myapplication.models.RequestMainMenu
import com.example.myapplication.models.RequestModelsUsers
import com.example.myapplication.models.RequestSubMenu
import com.example.myapplication.models.penerimaan.RequestPenerimaanEceran
import com.example.myapplication.models.penerimaan.penerimaaneceran.RequestGetTmpReceive
import com.example.myapplication.models.penerimaan.penerimaaneceran.RequestPostTmpReceive
import com.example.myapplication.models.penerimaan.penerimaaneceran.RequestReleaseRcpt
import com.example.myapplication.models.penerimaan.penerimaaneceran.RequestSelectLpn
import com.example.myapplication.models.penerimaan.penerimaaneceran.RequestSelectRcpt
import com.example.myapplication.models.penerimaan.penerimaaneceran.RequestTmpToReceipt
import com.example.myapplication.models.penerimaan.penerimaaneceran.RequestUpdateTmpReceive
import com.example.myapplication.models.penerimaan.penerimaaneceran.RequestValItem
import com.example.myapplication.models.penerimaan.penerimaaneceran.ResponseGetTmpReceive
import com.example.myapplication.models.pengambilan.all.RequestPickingCheckScanLoc
import com.example.myapplication.models.pengambilan.all.RequestPickingConfirmWave
import com.example.myapplication.models.pengambilan.all.RequestPickingGetTmpPick
import com.example.myapplication.models.pengambilan.all.RequestPickingModInValLPN
import com.example.myapplication.models.pengambilan.all.RequestPickingModLocItemList
import com.example.myapplication.models.pengambilan.all.RequestPickingModLocList
import com.example.myapplication.models.pengambilan.all.RequestPickingModWaveList
import com.example.myapplication.models.pengambilan.all.RequestPickingPackTypeList
import com.example.myapplication.models.pengambilan.all.RequestPickingPostToTmpPick
import com.example.myapplication.models.pengambilan.all.RequestPickingTmpToWQ
import com.example.myapplication.models.pengambilan.all.RequestPickingUpdateTmpPick
import com.example.myapplication.models.pengambilan.all.models.ModelsMessage
import com.example.myapplication.models.pengambilan.all.models.ModelsWQMessage
import com.example.myapplication.models.pengeluaran.pack.RequestDispModSoList
import com.example.myapplication.models.pengeluaran.pack.RequestDispModWaveList
import com.example.myapplication.models.penyimpanan.putawaybylpn.RequestGetPalletNumber
import com.example.myapplication.models.penyimpanan.putawaybylpn.RequestPutawayCheckLocation
import com.example.myapplication.models.penyimpanan.putawaybylpn.RequestPutawayGetAllocation
import com.example.myapplication.models.penyimpanan.putawaybylpn.RequestPutawayGetItemInLPN
import com.example.myapplication.models.penyimpanan.putawaybylpn.RequestPutawayGetMasterLocation
import com.example.myapplication.models.penyimpanan.putawaybylpn.RequestPutawayModDisplayLPN
import com.example.myapplication.models.penyimpanan.putawaybylpn.RequestSaveItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface RequestApi {
    @Headers("Content-Type: application/json")
    @POST("select_db.php")
    fun retrieveSelectDB(@Body dataPost:ModelsSelectDB): Call<ResponseSelectDB>

    @Headers("Content-Type: application/json")
    @POST("CekLogin.php")
    fun userLogin(@Body dataPost:RequestModelsUsers): Call<ResponseUserLogin>

    @Headers("Content-Type: application/json")
    @POST("LogoutUser.php")
    fun userLogout(@Body dataPost: RequestModelsUsers): Call<ResponseUserLogin>

    @Headers("Content-Type: application/json")
    @POST("get_main_menu.php")
    fun getMainMenu(@Body dataPost:RequestMainMenu): Call<ResponseGetMainMenu>

    @Headers("Content-Type: application/json")
    @POST("get_sub_menu.php")
    fun getSubMenu(@Body dataPost: RequestSubMenu): Call<ResponseGetSubMenu>

    //Penerimaan
    @Headers("Content-Type: application/json")
    @POST("penerimaan/get_penerimaan_eceran.php")
    fun getPenerimaanEceran(@Body dataPost: RequestPenerimaanEceran): Call<ResponseGetPenerimaanEceran>

    @Headers("Content-Type: application/json")
    @POST("penerimaan/penerimaan_eceran_proses.php")
    fun selectReceipt(@Body dataPost: RequestSelectRcpt): Call<ResponseRcptHeader>

    @Headers("Content-Type: application/json")
    @POST("penerimaan/penerimaan_eceran_proses.php")
    fun releaseReceipt(@Body dataPost: RequestReleaseRcpt): Call<ResponseRcptHeader>

    @Headers("Content-Type: application/json")
    @POST("penerimaan/penerimaan_eceran_proses.php")
    fun selectLPN(@Body dataPost: RequestSelectLpn): Call<ResponseRcptLPN>

    @Headers("Content-Type: application/json")
    @POST("penerimaan/get_eceran_sub_menu.php")
    fun getEceranSubMenu(@Body dataPost: RequestSubMenu): Call<ResponseGetSubMenu>

    @Headers("Content-Type: application/json")
    @POST("penerimaan/penerimaan_eceran_proses.php")
    fun eceranValItem(@Body dataPost: RequestValItem): Call<ResponseEceranValItem>

    @Headers("Content-Type: application/json")
    @POST("penerimaan/penerimaan_eceran_proses.php")
    fun eceranGetTmpReceive(@Body dataPost: RequestGetTmpReceive): Call<ResponseEceranGetTmpReceive>

    @Headers("Content-Type: application/json")
    @POST("penerimaan/penerimaan_eceran_proses.php")
    fun eceranPostTmpReceive(@Body dataPost: RequestPostTmpReceive): Call<ResponseEceranPostTmpReceive>

    @Headers("Content-Type: application/json")
    @POST("penerimaan/penerimaan_eceran_proses.php")
    fun updateTmpReceive(@Body dataPost: RequestUpdateTmpReceive): Call<ResponseRcptHeader>

    @Headers("Content-Type: application/json")
    @POST("penerimaan/penerimaan_eceran_proses.php")
    fun tmpToReceipt(@Body dataPost: RequestTmpToReceipt): Call<ResponseRcptHeader>
    //===========

    //Penyimpanan
    @Headers("Content-Type: application/json")
    @POST("penyimpanan/putaway_proses.php")
    fun putawayGetPalletNumber(@Body dataPost: RequestGetPalletNumber): Call<ResponsePutawayGetPalletNumber>

    @Headers("Content-Type: application/json")
    @POST("penyimpanan/putaway_proses.php")
    fun putawayGetAllocation(@Body dataPost: RequestPutawayGetAllocation): Call<ResponseDataPutawayGetAllocation>

    @Headers("Content-Type: application/json")
    @POST("penyimpanan/putaway_proses.php")
    fun putawayGetItemInLPN(@Body dataPost: RequestPutawayGetItemInLPN): Call<ResponseDataPutawayGetItemInLPN>

    @Headers("Content-Type: application/json")
    @POST("penyimpanan/putaway_proses.php")
    fun putawayGetMasterLocation(@Body dataPost: RequestPutawayGetMasterLocation): Call<ResponseDataPutawayGetMasterLocation>

    @Headers("Content-Type: application/json")
    @POST("penyimpanan/putaway_proses.php")
    fun putawayCheckLocation(@Body dataPost: RequestPutawayCheckLocation): Call<ResponseDataCheckLocation>

    @Headers("Content-Type: application/json")
    @POST("penyimpanan/putaway_proses.php")
    fun putawayModDisplayLPN(@Body dataPost: RequestPutawayModDisplayLPN): Call<ResponseDataPutawayModDisplayLPN>

    @Headers("Content-Type: application/json")
    @POST("penyimpanan/putaway_proses.php")
    fun putawaySaveItem(@Body dataPost: RequestSaveItem): Call<ResponseSaveItem>
    // End Penyimpanan

    // Pengambilan
    @Headers("Content-Type: application/json")
    @POST("pengambilan/picking_all_proses.php")
    fun pickingAllModWaveList(@Body dataPost: RequestPickingModWaveList): Call<ResponsePickingDataModWaveList>

    @Headers("Content-Type: application/json")
    @POST("pengambilan/picking_all_proses.php")
    fun pickingAllPackTypeList(@Body dataPost: RequestPickingPackTypeList): Call<ResponsePickingPackTypeList>

    @Headers("Content-Type: application/json")
    @POST("pengambilan/picking_all_proses.php")
    fun pickingAllCheckScanLPN(@Body dataPost: RequestPickingModInValLPN): Call<ResponsePickingModInValLPN>

    @Headers("Content-Type: application/json")
    @POST("pengambilan/picking_all_proses.php")
    fun pickingALlModLocList(@Body dataPost: RequestPickingModLocList): Call<ResponsePickingDataModLocList>

    @Headers("Content-Type: application/json")
    @POST("pengambilan/picking_all_proses.php")
    fun pickingAllCheckScanLoc(@Body dataPost: RequestPickingCheckScanLoc): Call<ResponsePickingCheckScanLoc>

    @Headers("Content-Type: application/json")
    @POST("pengambilan/picking_all_proses.php")
    fun pickingAllModLocItemList(@Body dataPost: RequestPickingModLocItemList): Call<ResponsePickingDataModLocItemList>

    @Headers("Content-Type: application/json")
    @POST("pengambilan/picking_all_proses.php")
    fun pickingAllGetTmpPick(@Body dataPost: RequestPickingGetTmpPick): Call<ResponsePickingGetTmpPick>

    @Headers("Content-Type: application/json")
    @POST("pengambilan/picking_all_proses.php")
    fun pickingAllPostToTmp(@Body dataPost: RequestPickingPostToTmpPick): Call<ModelsMessage>

    @Headers("Content-Type: application/json")
    @POST("pengambilan/picking_all_proses.php")
    fun pickingAllUpdateTmpPick(@Body dataPost: RequestPickingUpdateTmpPick): Call<ModelsMessage>

    @Headers("Content-Type: application/json")
    @POST("pengambilan/picking_all_proses.php")
    fun pickingALlTmpToWQ(@Body dataPost: RequestPickingTmpToWQ): Call<ModelsWQMessage>

    @Headers("Content-Type: application/json")
    @POST("pengambilan/picking_all_proses.php")
    fun pickingAllConfirmWave(@Body dataPost: RequestPickingConfirmWave) : Call<ModelsWQMessage>
    // End pengambilan

    // Pengaluaran
    @Headers("Content-Type: application/json")
    @POST("pengeluaran/dispatch_proses.php")
    fun dispatchPackModWaveList(@Body dataPost: RequestDispModWaveList): Call<ResponseDispModWaveList>

    @Headers("Content-Type: application/json")
    @POST("pengeluaran/dispatch_proses.php")
    fun dispatchPackModSoList(@Body dataPost: RequestDispModSoList): Call<ResponseDispModSoList>

    // End Pengeluaran
}