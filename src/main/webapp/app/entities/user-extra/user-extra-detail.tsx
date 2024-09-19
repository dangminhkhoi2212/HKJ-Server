import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-extra.reducer';

export const UserExtraDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userExtraEntity = useAppSelector(state => state.userExtra.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userExtraDetailsHeading">
          <Translate contentKey="serverApp.userExtra.detail.title">UserExtra</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userExtraEntity.id}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="serverApp.userExtra.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{userExtraEntity.phone}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="serverApp.userExtra.address">Address</Translate>
            </span>
          </dt>
          <dd>{userExtraEntity.address}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="serverApp.userExtra.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{userExtraEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="serverApp.userExtra.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{userExtraEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="serverApp.userExtra.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {userExtraEntity.createdDate ? <TextFormat value={userExtraEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="serverApp.userExtra.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{userExtraEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="serverApp.userExtra.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {userExtraEntity.lastModifiedDate ? (
              <TextFormat value={userExtraEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="serverApp.userExtra.user">User</Translate>
          </dt>
          <dd>{userExtraEntity.user ? userExtraEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-extra" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-extra/${userExtraEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserExtraDetail;
