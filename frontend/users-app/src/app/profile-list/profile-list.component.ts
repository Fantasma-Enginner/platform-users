import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { PerfilesService, ProfileDTO, State } from '@tsir/users-api';
import { Observable } from 'rxjs';
import swal from 'sweetalert2';

@Component({
  selector: 'users-profile-list',
  templateUrl: './profile-list.component.html',
  styleUrls: ['./profile-list.component.css']
})
export class ProfileListComponent implements OnInit {

  profiles: ProfileDTO[];

  searchForm: FormGroup;

  showEdit = false;
  profileId = 0;

  criteriaSelected: string;
  criteriaList: any[] = [
    { label: '', key: '' },
    { label: 'Código', key: 'CODE' },
    { label: 'Nombre', key: 'NAME' },
    { label: 'Estado', key: 'STATE' }
  ];
  stateList: string[] = [State.NUEVO, State.ACTIVO, State.INACTIVO, State.SUSPENDIDO];

  page = 1;
  count = 0;
  pageSize = 3;
  pageSizes = [3, 6, 9, 12];
  allPages = 0;

  constructor(
    private profileService: PerfilesService,
    private formBuilder: FormBuilder
  ) { }

  ngOnInit() {
    this.setSecurityConfiguration();
    this.initSearchForm();
  }

  private setSecurityConfiguration(): void {
    this.profileService.configuration.accessToken = localStorage.getItem('token');
  }

  private initSearchForm() {
    this.searchForm = this.formBuilder.group({
      criteria: [''],
      value: ['']
    });
  }

  isSelected(key: string): boolean {
    return this.searchForm.controls['criteria'].value == key;
  }

  numberOnly(event: any): boolean {
    const charCode = (event.which) ? event.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      return false;
    }
    return true;
  }

  resetForm() {
    this.searchForm.patchValue({
      value: ''
    })
    return true;
  }

  private getObservableFindProfiles(): Observable<HttpResponse<any>> {
    let paging = { "INDEX": this.page, "SIZE": this.pageSize };
    let filter = this.searchForm.value;
    let filtering = undefined;
    if (filter.criteria) {
      filtering = {};
      filtering[filter.criteria] = filter.value;
    }
    return this.profileService.findProfiles(filtering, paging, undefined, 'response');
  }

  findProfiles(): void {
    this.getObservableFindProfiles().subscribe(
      (_resp: HttpResponse<any>) => {
        this.profiles = _resp.body;
        this.count = Number.parseInt(_resp.headers.get('X-Paging-Elements'));
        this.allPages = Number.parseInt(_resp.headers.get('X-Paging-Pages'));
        if (!Object.keys(_resp.body).length) {
          swal.fire('Perfiles', 'No se encontraron perfiles con los criterios de búsqueda', 'info');
        }
      }
    )
  }

  handlePageChange(event: any) {
    this.page = event;
    this.findProfiles();
  }

  handlePageSizeChange(event: any) {
    this.pageSize = event.target.value;
    this.page = 1;
    this.findProfiles();
  }

  setProfileId(profileId: number): void {
    this.profileId = profileId;
    this.showEdit = true;
  }

  closeEdit(): void {
    this.showEdit = false;
  }

  isDisabledSearch(): boolean {
    let filter = this.searchForm.value;
    if (filter.criteria) {
      return filter.value ? false : true;
    } else {
      return false;
    }
  }

}
