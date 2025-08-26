<template>
  <view class="container">
    <!-- 顶部标题 -->
    <view class="header">
      <text />
      <text class="title">会员管理</text>
      <text class="add-btn" @click="handleAdd">新增</text>
    </view>

    <!-- 搜索区域 -->
    <view class="search-area">
      <view class="search-input">
        <view style="display: flex; align-items: center;">
          <image src="/static/images/index/search.png" class="search-icon" />
          <input v-model="searchText" placeholder="请输入搜索内容" />
        </view>
        <view class="search2">
          搜索
        </view>
      </view>
    </view>

    <!-- 导航标签 -->
    <view class="nav-tabs">
      <view v-for="(tab, index) in tabs" :key="index" :class="['tab-item', activeTab === index ? 'active' : '']"
        @click="activeTab = index">
        {{ tab }}
      </view>
    </view>

    <!-- 会员卡片 -->
    <view class="card-list" v-if="activeTab === 0">
      <view class="card" v-for="(member, index) in members" :key="index">
        <view class="card-row">
          <text class="label">姓名</text>
          <view class="value">{{ member.name }}</view>
        </view>
        <view class="card-row">
          <text class="label">年龄</text>
          <view class="value">{{ member.age }}</view>
        </view>
        <view class="card-row">
          <text class="label">性别</text>
          <view class="value">{{ member.gender }}</view>
        </view>
        <view class="card-row">
          <text class="label">身份证号</text>
          <view class="value">{{ member.idCard }}</view>
        </view>
        <view class="card-row">
          <text class="label">骑手证号</text>
          <view class="value">{{ member.riderCard }}</view>
        </view>
        <view class="card-row">
          <text class="label">电话</text>
          <view class="value">{{ member.phone }}</view>
        </view>
        <view class="card-actions">
          <view class="card-actions4">
            <text class="delete-btn" @click="handleDelete(index)">删除</text>
            <text class="edit-btn" @click="handleEdit(index)">修改</text>
          </view>
          <text v-if="editIndex === index" class="edit-btn2" @click="handleEditConfirm(index)">确认</text>
        </view>
      </view>
    </view>

    <!-- 马主卡片 -->
    <view class="card-list" v-if="activeTab === 1">
      <view class="card" v-for="(owner, index) in horseOwners" :key="index">
        <view class="card-row">
          <text class="label">姓名</text>
          <view class="value">{{ owner.name }}</view>
        </view>
        <view class="card-row">
          <text class="label">年龄</text>
          <view class="value">{{ owner.age }}</view>
        </view>
        <view class="card-row">
          <text class="label">性别</text>
          <view class="value">{{ owner.gender }}</view>
        </view>
        <view class="card-row">
          <text class="label">身份证号</text>
          <view class="value">{{ owner.idCard }}</view>
        </view>
        <view class="card-row">
          <text class="label">骑手证号</text>
          <view class="value">{{ owner.riderCard }}</view>
        </view>
        <view class="card-row">
          <text class="label">电话</text>
          <view class="value">{{ owner.phone }}</view>
        </view>
        <!-- 马匹信息 -->
        <view class="horse-info">
          <view class="sub-title">
            <text>马匹信息</text>
            <view @click="horseFlags(index)">

              <uni-icons v-if="horseIndex == index" type="up" size="20" color="#999" />
              <uni-icons v-else type="down" size="20" color="#999" />
            </view>
          </view>
          <view v-if="horseIndex == index">
            <view class="card-row">
              <text class="label">名字</text>
              <view class="value">{{ owner.horseName }}</view>
            </view>
            <view class="card-row">
              <text class="label">生日</text>
              <view class="value">{{ owner.horseBirthday }}</view>
            </view>
            <view class="card-row">
              <text class="label">血统</text>
              <view class="value">{{ owner.horseBloodline }}</view>
            </view>
            <view class="card-row">
              <text class="label">电话</text>
              <view class="value">{{ owner.phone }}</view>
            </view>
            <view class="card-row">
              <text class="label">马匹护照</text>
              <view class="value">{{ owner.horseHuzhao }}</view>
            </view>
          </view>
        </view>
        <view class="card-actions">
          <view class="card-actions4">
            <text class="delete-btn" @click="handleDeleteHorseOwner(index)">删除</text>
            <text class="edit-btn" @click="handleEditHorseOwner(index)">修改</text>
          </view>
          <text v-if="editHorseOwnerIndex === index" class="edit-btn2"
            @click="handleEditHorseOwnerConfirm(index)">确认</text>
        </view>
      </view>
    </view>

    <!-- 家庭卡片 -->
    <view class="card-list" v-if="activeTab === 2">
      <view class="card" v-for="(family, index) in families" :key="index">
        <view class="card-row">
          <text class="label">类别</text>
          <view class="value">{{ family.type }}</view>
        </view>
        <view class="card-row">
          <text class="label">姓名</text>
          <view class="value">{{ family.name }}</view>
        </view>
        <view class="card-row">
          <text class="label">年龄</text>
          <view class="value">{{ family.age }}</view>
        </view>
        <view class="card-row">
          <text class="label">性别</text>
          <view class="value">{{ family.gender }}</view>
        </view>
        <view class="card-row">
          <text class="label">身份证号</text>
          <view class="value">{{ family.idCard }}</view>
        </view>
        <view class="card-row">
          <text class="label">骑手证号</text>
          <view class="value">{{ family.riderCard }}</view>
        </view>
        <view class="card-row">
          <text class="label">电话</text>
          <view class="value">{{ family.phone }}</view>
        </view>
        <view class="card-actions1">
          <view class="card-actions2">
            <text class="delete-btn" @click="handleDeleteFamily(index)">删除</text>
            <text class="edit-btn" @click="handleEditFamily(index)">修改</text>
            <text class="add-member-btn" @click="handleAddFamilyMember(index)">新增家庭成员</text>
          </view>
          <view v-if="editFamilyIndex == index">
            <text class="edit-btn2" @click="handleEditFamilyConfirm(index)">确认</text>
          </view>
          <view v-else @click="familyFlags(index)" style="display: flex;align-items: center;">

            <uni-icons v-if="familyIndex == index" type="up" size="20" color="#999" />
            <uni-icons v-else type="down" size="20" color="#999" />
          </view>
        </view>

        <!-- 家庭成员列表 -->
        <view class="family-members" v-if="family.members && family.members.length && familyIndex == index">
          <view class="member-card" v-for="(member, memberIndex) in family.members" :key="memberIndex">
            <view class="card-row">
              <text class="label">类别</text>
              <view class="value">{{ member.type }}</view>
            </view>
            <view class="card-row">
              <text class="label">身份</text>
              <view class="value">{{ member.role }}</view>
            </view>
            <view class="card-row">
              <text class="label">姓名</text>
              <view class="value">{{ member.name }}</view>
            </view>
            <view class="card-row">
              <text class="label">年龄</text>
              <view class="value">{{ member.age }}</view>
            </view>
            <view class="card-row">
              <text class="label">性别</text>
              <view class="value">{{ member.gender }}</view>
            </view>
            <view class="card-row">
              <text class="label">身份证号</text>
              <view class="value">{{ family.idCard }}</view>
            </view>
            <view class="card-row">
              <text class="label">骑手证号</text>
              <view class="value">{{ family.riderCard }}</view>
            </view>
            <view class="card-row">
              <text class="label">电话</text>
              <view class="value">{{ family.phone }}</view>
            </view>
            <view class="card-actions">
              <view class="card-actions4">
                <text class="delete-btn" @click="handleDeleteFamilyMember(index, memberIndex)">删除</text>
                <text class="edit-btn" @click="handleEditFamilyMember(index, memberIndex)">修改</text>
              </view>
              <text v-if="editFamilyMemberIndex === index" class="edit-btn2"
                @click="handleEditFamilyMemberConfirm(index, memberIndex)">确认</text>
            </view>
          </view>
        </view>

        <view class="add-family-member" />
      </view>
    </view>
  </view>
  <CustomTabbar />
</template>

<script>
import CustomTabbar from '@/components/custom-tabbar/custom-tabbar.vue';
export default {
  components: {
    CustomTabbar
  },
  data() {
    return {
      tabs: ['会员', '马主', '家庭'],
      activeTab: 0,
      horseFlag: false,
      horseIndex: -1,
      familyIndex: -1,
      editIndex: -1,
      editHorseOwnerIndex: -1,
      editFamilyIndex: -1,
      editFamilyMemberIndex: -1,
      memberForm: {
        name: '',
        age: '',
        gender: '',
        idCard: '',
        riderCard: '',
        phone: ''
      },
      members: [
        {
          name: '某某某',
          age: '26',
          gender: '女',
          idCard: '565956487225896321',
          riderCard: '89741921965',
          phone: '18974191965'
        },
        {
          name: '某某某',
          age: '26',
          gender: '女',
          idCard: '565956487225896321',
          riderCard: '89741921965',
          phone: '18974191965'
        },
      ],
      horseOwners: [
        {
          name: '某某某',
          age: '26',
          gender: '女',
          idCard: '565956487225896321',
          riderCard: '89741921965',
          phone: '18974191965',
          horseName: '小马名字',
          horseBirthday: '2006.06.06',
          horseBloodline: 'KWPN',
          horseHuzhao: '123456789012345678'
        },
        {
          name: '某某某',
          age: '26',
          gender: '女',
          idCard: '565956487225896321',
          riderCard: '89741921965',
          phone: '18974191965',
          horseName: '小马名字',
          horseBirthday: '2006.06.06',
          horseBloodline: 'KWPN',
          horseHuzhao: '123456789012345678'
        },
      ],
      families: [
        {
          type: '马主',
          name: '某某某',
          age: '26',
          gender: '女',
          idCard: '565956487225896321',
          riderCard: '89741921965',
          phone: '18974191965',
          members: [
            {
              type: '会员',
              role: '配偶',
              name: '某某某',
              age: '26',
              gender: '女'
            }
          ]
        }
      ]
    }
  },
  methods: {
    horseFlags(index) {
      if (this.horseIndex == index) {
        this.horseIndex = -1
      } else {
        this.horseIndex = index
      }
    },
    familyFlags(index) {
      if (this.familyIndex == index) {
        this.familyIndex = -1
      } else {
        this.familyIndex = index
      }
    },
    handleAdd() {
      // 新增逻辑
      console.log('新增按钮点击')
      if (this.activeTab == 0) {
        this.members.push(this.memberForm)
      }
    },
    handleDelete(index) {
      // 删除会员逻辑
      this.members.splice(index, 1)
    },
    handleEdit(index) {
      // 修改会员逻辑
      this.editIndex = index
      this.memberForm = this.members[index]
      console.log('修改会员', index)
    },
    handleEditConfirm(index) {
      // 确认修改会员逻辑
      this.editIndex = -1
      this.members[index] = this.memberForm
    },
    handleDeleteHorseOwner(index) {

      // 删除马主逻辑
      this.horseOwners.splice(index, 1)
    },
    handleEditHorseOwner(index) {
      // 修改马主逻辑
      this.editHorseOwnerIndex = index
      this.horseOwnerForm = this.horseOwners[index]
      console.log('修改马主', index)
    },
    handleEditHorseOwnerConfirm(index) {
      // 确认修改马主逻辑
      this.editHorseOwnerIndex = -1
      this.horseOwners[index] = this.horseOwnerForm
    },
    handleDeleteFamily(index) {
      // 删除家庭逻辑
      this.families.splice(index, 1)
    },
    handleEditFamily(index) {
      // 修改家庭逻辑
      this.editFamilyIndex = index
      this.familyForm = this.families[index]
      console.log('修改家庭', index)
    },
    handleEditFamilyConfirm(index) {
      // 确认修改家庭逻辑
      this.editFamilyIndex = -1
      this.families[index] = this.familyForm
    },
    handleAddFamilyMember(index) {
      // 新增家庭成员逻辑
      console.log('新增家庭成员', index)
    },
    handleDeleteFamilyMember(familyIndex, memberIndex) {
      // 删除家庭成员逻辑
      this.families[familyIndex].members.splice(memberIndex, 1)
    },
    handleEditFamilyMember(familyIndex, memberIndex) {
      // 修改家庭成员逻辑
      console.log('修改家庭成员', familyIndex, memberIndex)
      this.editFamilyMemberIndex = familyIndex
      this.editFamilyMemberForm = this.families[familyIndex].members[memberIndex]
    },

    handleEditFamilyMemberConfirm(familyIndex, memberIndex) {
      // 确认修改家庭成员逻辑
      console.log('确认修改家庭成员', familyIndex, memberIndex)
      this.editFamilyMemberIndex = -1
    }
  }
}
</script>

<style>
.container {
  padding: 32rpx;
  background: linear-gradient(180deg, #000000 31%, rgba(254, 254, 254, 0.38) 79%);
  min-height: 100vh;
}

/* 顶部标题 */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32rpx;
  margin-top: 150rpx;
}

.title {
  font-family: "Alibaba PuHuiTi 2.0", "Alibaba PuHuiTi 20";
  font-weight: 700;
  font-size: 36rpx;
  color: #f4f5f9;
  line-height: 52rpx;
}

.add-btn {
  font-size: 28rpx;
  color: #f4f5f9;
  border: 1rpx dashed #f4f5f9;
  padding: 4rpx 2rpx;
}

/* 搜索区域 */
.search-area {
  margin-bottom: 32rpx;
}

.search-input {
  height: 72rpx;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: #fff;
  padding: 0 5rpx 0 24rpx;
}

.search2 {
  width: 116rpx;
  height: 56rpx;
  background: linear-gradient(90deg, #FEDEA7 0%, #F4B753 100%);
  border-radius: 8rpx;
  font-size: 28rpx;
  color: #3A2605;
  line-height: 56rpx;
  text-align: center;
}

.search-icon {
  width: 32rpx;
  height: 32rpx;
  margin-right: 16rpx;
}

.placeholder {
  color: #999;
  font-size: 28rpx;
}

/* 导航标签 */
.nav-tabs {
  display: flex;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 16rpx;
  margin-bottom: 32rpx;
  background-color: #fff;
  padding: 4rpx;
}

.tab-item {
  width: 234rpx;
  height: 72rpx;
  border-radius: 14rpx;
  font-size: 28rpx;
  color: #999999;
  line-height: 40rpx;
  display: flex;
  justify-content: center;
  align-items: center;
}

.tab-item.active {
  background: #F6F2EB;
  border-radius: 8rpx;
  font-weight: 600;
  color: #B7975E;
}

/* 卡片列表 */
.card-list {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
  margin-bottom: 100rpx;
}

.card {
  background: #FFFFFF;
  box-shadow: 0rpx 0rpx 14rpx 0rpx rgba(204, 204, 204, 0.46);
  border-radius: 20rpx;
  padding: 32rpx;
}

.card-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24rpx;
}

.label {
  font-size: 28rpx;
  color: #666666;
  line-height: 40rpx;
  width: 140rpx;
}

.value {
  height: 60rpx;
  background: #F5F5F5;
  border-radius: 8rpx;
  font-size: 28rpx;
  color: #333333;
  line-height: 40rpx;
  padding: 0 20rpx;
  display: flex;
  align-items: center;
}

.card-actions1 {
  display: flex;
  justify-content: space-between;
  gap: 24rpx;
}

.card-actions {
  display: flex;
  justify-content: space-between;
  gap: 24rpx;
  margin-top: 32rpx;
}

.card-actions2 {
  display: flex;
  justify-content: flex-start;
  gap: 24rpx;
}

.card-actions4 {
  display: flex;
  justify-content: flex-start;
  gap: 24rpx;
}

.delete-btn {
  width: 92rpx;
  height: 60rpx;
  border-radius: 8rpx;
  border: 1rpx solid #999999;
  font-size: 28rpx;
  color: #999999;
  line-height: 40rpx;
  display: flex;
  justify-content: center;
  align-items: center;
}

.edit-btn {
  width: 92rpx;
  height: 60rpx;
  border-radius: 8rpx;
  border: 1rpx solid #666666;
  font-size: 28rpx;
  color: #666666;
  line-height: 40rpx;
  display: flex;
  justify-content: center;
  align-items: center;
}

.edit-btn2 {
  width: 92rpx;
  height: 60rpx;
  border-radius: 8rpx;
  border: 1rpx solid #B7975E;
  font-size: 28rpx;
  color: #B7975E;
  line-height: 40rpx;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 马匹信息 */
.horse-info {
  margin-top: 30rpx;
}

.sub-title {
  font-size: 28rpx;
  color: #999999;
  margin-bottom: 24rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 家庭成员 */
.family-members {
  margin-top: 32rpx;
  border-top: 1rpx solid #eee;
  padding-top: 32rpx;
}

.member-card {
  background: #f9f9f9;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 24rpx;
}

.add-family-member {
  margin-top: 32rpx;
  display: flex;
  justify-content: center;
}

.add-member-btn {
  color: #B7975E;
  font-size: 28rpx;
  border: 1rpx dashed #B7975E;
  padding: 16rpx 32rpx;
  border-radius: 8rpx;
}
</style>